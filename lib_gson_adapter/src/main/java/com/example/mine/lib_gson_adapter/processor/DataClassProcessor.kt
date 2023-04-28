package com.example.mine.lib_gson_adapter.processor


import com.example.lib_annotation.PokeBean
import com.example.mine.lib_gson_adapter.KEY_NULL_SAFE
import com.example.mine.lib_gson_adapter.scan.DataClassScanner
import com.example.mine.lib_gson_adapter.scan.KmClassConvert
import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.*
import com.example.mine.lib_gson_adapter.scan.KaptKtType
import com.example.mine.lib_gson_adapter.typeadapter.TypeAdapterFactoryClassGeneratorImpl
import com.example.mine.lib_gson_adapter.typeadapter.TypeAdapterGeneratorImpl
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

/**
 * Day：2023/4/27 11:54
 * @author zhanglei
 */
@AutoService(Processor::class)
class DataClassProcessor : AbstractProcessor() {

    private val logger by lazy {
        Logger(processingEnv)
    }

    private var round: Int = 0

    private val RoundEnvironment.annotatedClasses: Sequence<TypeElement>
        get() {
            return getElementsAnnotatedWith(PokeBean::class.java)
                .asSequence()
                .filter { it.kind == ElementKind.CLASS }
                .filterNot { processingEnv.elementUtils.getPackageOf(it).isUnnamed }
                .map {
                    it as TypeElement
                }
        }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(PokeBean::class.java.name)
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        logger.i("start process >>> round:${round}")
        round++
        if (annotations == null || roundEnv == null) {
            return true
        }

        val kmClassConvert = KmClassConvert(logger)

        val start = System.currentTimeMillis()
        val debugList = roundEnv.annotatedClasses.toList()
        for (i in debugList.indices) {
            val element = debugList[i]
            logger.d("element:${element}")
            //泛型信息
            logger.d("--|typeParameters:${element.typeParameters}")
            //包名+类名
            logger.d("--|qualifiedName:${element.qualifiedName}")
            //类名
            logger.d("--|simpleName:${element.simpleName}")
            //父类
            logger.d("--|getSuperclass:${element.superclass}")
            //类型 ElementKind
            logger.d("--|kind:${element.kind}")

            //修饰符  public final 等
            val modifierIterator = element.modifiers.iterator()
            while (modifierIterator.hasNext()) {
                val m = modifierIterator.next()
                logger.d("--|modifiers:$m")
            }

            //包裹所有子元素：FIELD、METHOD
            val enclosedElementIterator = element.enclosedElements.iterator()
            while (enclosedElementIterator.hasNext()) {
                val e = enclosedElementIterator.next()
                logger.d("--|enclosedElements:${e.simpleName}")
                logger.d("----|kind:${e.kind}")
                //包裹内容：类型
                logger.d("----|type:${e.asType()}")
            }
        }
        //解析每一个注解Data class
        val classScanners = roundEnv.annotatedClasses.map {
            DataClassScanner(processingEnv, it, kmClassConvert, logger)
        }.toList()

        if (classScanners.isEmpty()) {
            logger.i("end process >>> round=$round timeCost = ${System.currentTimeMillis() - start}ms")
            return false
        }

        logger.i(">>>>>>>>>>>> data classScanners complete <<<<<<<<<<<<<<")

        val classFilter = classScanners.map { it.classType }.toSet()

        val scannerToTypeSpecs = classScanners.map { classScanner ->
            logger.i("---|---|---| start ${classScanner.classType.to2String()} |---|---|---|---")

            val classType = classScanner.classType
            classScanner.field.forEach {
                logger.i(" classScanner field ${classScanner.classKind} ${classType.to2String()} >>> ${it.to2String()}")
            }
            val typeAdapterClassGenerator = TypeAdapterGeneratorImpl(logger)
            val typeAdapterClassGenConfig = TypeAdapterClassGenConfig(
                nullSafe = processingEnv.options[KEY_NULL_SAFE] == true.toString()
            )
            val typeSpec = typeAdapterClassGenerator.generate(
                classScanner,
                classFilter,
                typeAdapterClassGenConfig
            ).toBuilder().apply {
                (classType as IElementOwner).target?.let { addOriginatingElement(it) }
            }.build()

            logger.i("---|---|---| end ${classScanner.classType.to2String()} |---|---|---|---")
            classScanner to typeSpec
        }

        scannerToTypeSpecs.map { (classScanner, typeSpec) ->
            val className = classScanner.classType.asTypeName() as ClassName

            logger.i("FileSpec builder ${className.packageName} ${typeSpec.name}")

            FileSpec.builder(className.packageName, typeSpec.name.toString())
                .addType(typeSpec)
                .indent(" ".repeat(4))
                .build()
        }.forEach {
            it.writeTo(processingEnv.filer)
        }

        generateTypeAdapterFactoryIfNeed(scannerToTypeSpecs)

        logger.i("end process <<< round:${round} duration:${System.currentTimeMillis() - start}")
        return true
    }

    private fun generateTypeAdapterFactoryIfNeed(
        scannerToTypeSpecs: List<Pair<ClassScanner, TypeSpec>>
    ) {
//        if (!processingEnv.options.containsKey(KEY_TYPE_ADAPTER_FACTORY_NAME)) {
//            return
//        }
//        val typeAdapterFactoryName = processingEnv.options[KEY_TYPE_ADAPTER_FACTORY_NAME]!!
//        if (typeAdapterFactoryName.isEmpty()) {
//            throw IllegalArgumentException("Invalid $KEY_TYPE_ADAPTER_FACTORY_NAME param")
//        }
        val typeAdapterFactoryName = "com.example.minedemo.data.PokeBeanFactory"
        logger.i("generate typeAdapterFactory: $typeAdapterFactoryName")
        val classToTypeAdapters = scannerToTypeSpecs.map { (classScanner, typeSpec) ->
            val classKtType = classScanner.classType
            val typeAdapterPackageName = (classKtType.asTypeName() as ClassName).packageName
            val typeAdapterSimpleName = typeSpec.name.toString()
            val typeAdapterKtType = KaptKtType(
                rawType = ClassName(typeAdapterPackageName, typeAdapterSimpleName).canonicalName,
                nullable = false,
                variance = Variance.INVARIANT,
                jsonTokenName = JsonTokenDelegate.OBJECT,
                generics = listOf(),
                target = null
            )
            classKtType to typeAdapterKtType
        }.toSet()

        val typeAdapterFactorySpec = TypeAdapterFactoryClassGeneratorImpl(logger)
            .generate(typeAdapterFactoryName, classToTypeAdapters)
            .toBuilder().apply {
                classToTypeAdapters.mapNotNull { (classKtType, _) ->
                    (classKtType as? IElementOwner)?.target
                }.forEach { addOriginatingElement(it) }
            }.build()
        val typeAdapterFactoryClassName = ClassName.bestGuess(typeAdapterFactoryName)

        FileSpec.builder(
            typeAdapterFactoryClassName.packageName,
            typeAdapterFactoryClassName.simpleName
        ).addType(typeAdapterFactorySpec)
            .indent(" ".repeat(4))
            .build()
            .writeTo(processingEnv.filer)
    }
}