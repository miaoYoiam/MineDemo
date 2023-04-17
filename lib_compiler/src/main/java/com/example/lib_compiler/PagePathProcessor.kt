package com.example.lib_compiler

import com.example.lib_annotation.PagePath
import com.example.lib_annotation.PageType
import com.example.utils.ProcessorUtils
import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

/**
 * Day：2020/12/1 5:22 PM
 * @author zhanglei
 *
 * [AbstractProcessor] 编译时扫描和处理注解
 */

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.example.lib_annotation.PagePath")
class PagePathProcessor : AbstractProcessor() {

    private var typeUtils: Types? = null
    private var elementUtils: Elements? = null
    private var filer: Filer? = null
    private var message: Messager? = null
    private var proEnv: ProcessingEnvironment? = null

    private val annotationMap = hashMapOf<String, PagePathProxy>()

    private var hasProcessed = false

    /**
     * @see ProcessingEnvironment 可以提供工具类 Elements Types Filer
     */
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        proEnv = processingEnv
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filer = processingEnv.filer
        message = processingEnv.messager
    }

    /**
     * 兼容
     */
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val annotations = mutableSetOf<String>()
        annotations.add(PagePath::class.java.canonicalName)
        return annotations
    }

    /**
     * 处理主函数
     */
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        if (hasProcessed) return true

        val elements = roundEnv?.getElementsAnnotatedWith(PagePath::class.java) ?: return false
        for (annotatedElement in elements) {
            if (annotatedElement.kind != ElementKind.CLASS) {
                throw ProcessingException(
                    annotatedElement, "Only classes can be annotated with @%s",
                    PagePath::class.java.simpleName
                )
            }

            val typeElement = annotatedElement as TypeElement
            val annotatedClass = PagePathProxy(typeElement)
            val id = annotatedClass.id
            val canonicalName = annotatedClass.getTypeElement().javaClass.canonicalName

            checkValidClass(annotatedClass)

            if (id.isNullOrEmpty()) {
                errorLog(
                    annotatedClass.getTypeElement(),
                    "Conflict: The class $canonicalName  path is empty"
                )
                continue
            }
            if (annotationMap.containsKey(id)) {
                errorLog(
                    annotatedClass.getTypeElement(),
                    "Conflict: The class ${annotationMap[id]?.getTypeElement()?.javaClass?.canonicalName} is annotated with path ='${id}' but $canonicalName already uses the same path"
                )
                continue
            }

            annotationMap[id] = annotatedClass
        }

        val typeSpec = generatePageAnnotation()

        hasProcessed = true

        if (typeSpec != null) {
            proEnv?.let {
                val packageFullName = ROUTER_PACKAGE
                ProcessorUtils.writeIndexer(typeSpec.build(), it)
//                try {
//                    val javaFile = JavaFile.builder(packageFullName, typeSpec.build()).build()
//                    javaFile.writeTo(filer)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
            }
        }

        return true
    }

    private fun generatePageAnnotation(): TypeSpec.Builder? {
        var typeSpec: TypeSpec.Builder? = null
        try {
            val pageNode = ClassName.get("com.example.data", "PageNode")
            val string = ClassName.get("java.lang", "String")
            val pageType = ClassName.get("com.example.lib_annotation", "PageType")
            val hashMap = ClassName.get("java.util", "HashMap")
            val hashMapType = ParameterizedTypeName.get(hashMap, string, pageNode)

            typeSpec = TypeSpec.classBuilder(ROUTER_NAME).addModifiers(Modifier.PUBLIC)
            val paramsSpec = FieldSpec.builder(hashMapType, FIELD_NAME).addModifiers(
                Modifier.PUBLIC,
                Modifier.STATIC
            ).initializer(CodeBlock.builder().add("new HashMap<>()").build())
                .build()
            typeSpec?.addField(paramsSpec)

            val methodSpec = MethodSpec.methodBuilder("init").addModifiers(
                Modifier.PUBLIC,
                Modifier.STATIC
            )

            val iterator = annotationMap.entries.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                val id = item.value.id
                val type = item.value.type
                val allowMultiMode = item.value.allowMultiMode
                val qualifiedName = item.value.getTypeElement().qualifiedName.toString()

                val codeBlock = CodeBlock.builder().add(
                    "$FIELD_NAME.put(\$S,new PageNode(\$S,\$T.$type,$allowMultiMode))",
                    qualifiedName,
                    id,
                    pageType
                ).build()
                methodSpec.addStatement(codeBlock)
            }
            typeSpec?.addMethod(methodSpec.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return typeSpec
    }

    private fun checkValidClass(item: PagePathProxy) {

        val classElement: TypeElement = item.getTypeElement()
        if (!classElement.modifiers.contains(Modifier.PUBLIC)) {
            throw ProcessingException(
                classElement,
                "The class %s is not public.",
                classElement.qualifiedName.toString()
            )
        }

        if (classElement.modifiers.contains(Modifier.ABSTRACT)) {
            throw ProcessingException(
                classElement,
                "The class %s is abstract. You can't annotate abstract classes with @%",
                classElement.qualifiedName.toString(), PagePath::class.java.simpleName
            )
        }

        for (enclosed in classElement.enclosedElements) {
            if (enclosed.kind == ElementKind.CONSTRUCTOR) {
                val constructorElement = enclosed as ExecutableElement
                if (constructorElement.parameters.size == 0 && constructorElement.modifiers
                        .contains(Modifier.PUBLIC)
                ) {
                    return
                }
            }
        }

        throw ProcessingException(
            classElement,
            "The class %s must provide an public empty default constructor",
            classElement.qualifiedName.toString()
        )
    }

    fun errorLog(e: Element?, msg: String?) {
        message?.printMessage(Diagnostic.Kind.ERROR, msg, e)
    }

    fun noteLog(msg: String?) {
        message?.printMessage(Diagnostic.Kind.NOTE, msg)
    }


    companion object {
        const val ROUTER_PACKAGE = "com.maetimes.annotation"
        const val ROUTER_NAME = "PageAnnotationWrapper"
        const val FIELD_NAME = "mPageMap"
    }
}