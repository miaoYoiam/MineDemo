package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.base.*
import com.example.mine.lib_gson_adapter.modifierStatic
import com.example.mine.lib_gson_adapter.modifierTransient
import com.example.mine.lib_gson_adapter.scan.resolver.KmClassKindResolver
import com.example.mine.lib_gson_adapter.scan.resolver.KmPropertyResolver
import com.example.mine.lib_gson_adapter.scan.resolver.KmValueParameterResolver
import kotlinx.metadata.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*

/**
 * Day：2023/4/27 16:00
 * @author zhanglei
 */
class DataClassScanner(
    private val processingEnvironment: ProcessingEnvironment,
    private val targetClass: TypeElement,
    private val kmClassConvert: KmClassConvert,
    private val logger: Logger
) : ClassScanner() {
    private val kmClass: KmClass by lazy {
        kmClassConvert.get(targetClass)
    }

    override val classKind: ClassKind by lazy {
        logger.i("---| DataClassScanner getClassKind")
        KmClassKindResolver(targetClass, kmClassConvert, logger).getClassKind()
    }

    override val classType: ElementType by lazy {
        logger.i("---| DataClassScanner classType")
        resolveType()
    }

    override val field: List<ElementFiled> by lazy {
        scanPrimaryConstructor() + scanBody() + scanSupers()
    }

    private fun resolveType(): ElementType {
        val generics = targetClass.typeParameters.map {
            VariableType(
                rawType = it.simpleName.toString(),
                nullable = false,
                variance = Variance.INVARIANT,
                jsonTokenName = JsonTokenDelegate.OBJECT,
                generics = listOf()
            )
        }

        val type = KaptKtType(
            rawType = targetClass.qualifiedName.toString(),
            nullable = false,
            variance = Variance.INVARIANT,
            jsonTokenName = JsonTokenDelegate.OBJECT,
            generics = generics,
            target = targetClass
        )

        logger.i("---| resolveClassKtType：${type}")

        return type
    }


    /**
     * VariableElement:用于解析和处理Java源代码中的注解信息，以及生成新的Java源代码
     *
     * KmValueParameter:表示函数或构造函数的参数
     */
    private fun scanPrimaryConstructor(): List<ElementFiled> {
        logger.i("---| scanPrimaryConstructor start")
        if (targetClass.kind != ElementKind.CLASS) {
            return emptyList()
        }

        //获取主构造函数
        val primaryConstructor = kmClass.constructors.single {
            !Flag.Constructor.IS_SECONDARY(it.flags)
        }
        //获取主构造函数里面的参数
        val list = primaryConstructor.valueParameters.asSequence().filter {
            val element = findVariableElement(it.name)
            element != null && !element.modifierTransient()
        }.map {
            logger.w("---| KmValueParameterResolver start ${it.name}")
            /**
             * KtValueField(isFinal=true, fieldName=stringValue, keys=[foo_string], type=KaptKtType(rawType=kotlin.String, nullable=false, variance=INVARIANT, jsonTokenName=STRING, generics=[], target=stringValue), initializer=DEFAULT, declarationScope=PRIMARY_CONSTRUCTOR, transient=false, target=stringValue)
             */
            val value = KmValueParameterResolver(
                processingEnvironment = processingEnvironment,
                targetClass = targetClass,
                variableElement = findVariableElement(it.name),
                kmValueParameter = it,
                logger = logger
            ).resolveKmValueParameter()
            logger.w("---| KmValueParameterResolver end：${value}")
            value
        }.toList()
        logger.i("---| scanPrimaryConstructor end")
        return list
    }

    private fun findVariableElement(fieldName: String): VariableElement? {
        return targetClass.enclosedElements.asSequence().filter {
            it.kind == ElementKind.FIELD
        }.filterNot {
            it.modifierStatic() || it.modifierTransient()
        }.find {
            val elementName = it.simpleName.toString()
//            logger.i("---| findVariableElement： elementName：${elementName}  fieldName：${fieldName}")
            elementName == fieldName
        } as? VariableElement
    }

    private fun resolveKtField(kmProperty: KmProperty): ElementFiled {
        val resolver = KmPropertyResolver(
            processingEnvironment,
            targetClass,
            findVariableElement(kmProperty.name),
            kmProperty
        ).resolveKmProperty()

        logger.i("---| resolveKtField：${resolver}")

        return resolver
    }

    private fun scanBody(): List<ElementFiled> {
        logger.i("---| scanBody start")
        val constructorFieldNames = kmClass.constructors.map { kmConstructor ->
            kmConstructor.valueParameters.map { it.name }
        }.flatten().distinct()
        //剔除在主构造函数里面出现的参数
        val list = kmClass.properties.asSequence().filter {
            logger.i("---| scanBody name :${it.name}")
            it.name !in constructorFieldNames
        }.filter {
            val aptVariableElement = findVariableElement(it.name)
            if (classKind == ClassKind.INTERFACE) {
                true
            } else {
                aptVariableElement != null && !aptVariableElement.modifierTransient()
            }
        }.map {
            resolveKtField(it)
        }.toList()

        logger.i("---| scanBody end :${list}")

        return list
    }

    private fun scanSupers(): List<ElementFiled> {
        logger.i("---| scanSupers start")
        val list = kmClass.supertypes.asSequence().mapNotNull {
            val classifier = it.classifier as? KmClassifier.Class
            if (classifier == null) {
                logger.e("Unexpected super type ${targetClass.qualifiedName}", targetClass)
                throw IllegalStateException(
                    "Unexpected super type ${targetClass.qualifiedName}"
                )
            }
            classifier.name.replace("/", ".")
        }.filter {
            it != Any::class.qualifiedName
        }.map { name ->
            val typeElement = processingEnvironment.elementUtils.getTypeElement(name)
            if (typeElement == null) {
                logger.e("Unexpected super type ${targetClass.qualifiedName}", targetClass)
                throw IllegalStateException(
                    "Unexpected super type ${targetClass.qualifiedName}"
                )
            } else {
                typeElement
            }
        }.map { superTypeElement ->
            DataClassScanner(
                processingEnvironment,
                superTypeElement,
                kmClassConvert,
                logger
            )
        }.map { classScanner ->
            val superTypeElement = classScanner.targetClass
            val declarationScope = if (superTypeElement.kind == ElementKind.CLASS) {
                DeclarationScope.SUPER_CLASS
            } else {
                DeclarationScope.SUPER_INTERFACE
            }
            classScanner.field.map { it.copy(declarationScope = declarationScope) }
        }.flatten().toList()

        logger.i("---| scanSupers end：${list}")

        return list
    }

}