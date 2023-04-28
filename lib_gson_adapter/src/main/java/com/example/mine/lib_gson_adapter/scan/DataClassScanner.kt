package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.base.*
import kotlinx.metadata.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.*

/**
 * Day：2023/4/27 16:00
 * @author zhanglei
 */
class DataClassScanner(
    private val processingEnvironment: ProcessingEnvironment,
    private val belongingClass: TypeElement,
    private val kmClassConvert: KmClassConvert,
    private val logger: Logger
) : ClassScanner() {
    private val kmClass: KmClass by lazy {
        kmClassConvert.get(belongingClass)
    }

    override val cKind: ClassKind
        get() = KmClassKindResolver(belongingClass, kmClassConvert, logger).getClassKind()

    override val classType: IType
        get() = resolveType()

    override val field: List<IFiled>
        get() = scanPrimaryConstructor() + scanBody() + scanSupers()

    private fun resolveType(): IType {
        val generics = belongingClass.typeParameters.map {
            VariableType(
                rawType = it.simpleName.toString(),
                nullable = false,
                variance = Variance.INVARIANT,
                jsonTokenName = JsonTokenDelegate.OBJECT,
                generics = listOf()
            )
        }

        val type = KaptKtType(
            rawType = belongingClass.qualifiedName.toString(),
            nullable = false,
            variance = Variance.INVARIANT,
            jsonTokenName = JsonTokenDelegate.OBJECT,
            generics = generics,
            target = belongingClass
        )

        logger.i("---| resolveClassKtType：${type}")

        return type
    }

    private fun scanPrimaryConstructor(): List<IFiled> {
        if (belongingClass.kind != ElementKind.CLASS) {
            return emptyList()
        }

        //获取主构造函数
        val primaryConstructor = kmClass.constructors.single {
            !Flag.Constructor.IS_SECONDARY(it.flags)
        }
        //获取参数 除了 transient 的
        val list = primaryConstructor.valueParameters.asSequence().filter {
            val aptVariableElement = findAptVariableElement(it.name)
            aptVariableElement != null && !aptVariableElement.modifiers.contains(Modifier.TRANSIENT)
        }.map {
            resolveKtField(it)
        }.toList()

        logger.i("---| scanPrimaryConstructor：${list}")

        return list
    }

    private fun findAptVariableElement(fieldName: String): VariableElement? {
        return belongingClass.enclosedElements.asSequence().filter {
            it.kind == ElementKind.FIELD
        }.filterNot {
            it.modifiers.contains(Modifier.STATIC) || it.modifiers.contains(Modifier.TRANSIENT)
        }.find {
            val elementName = it.simpleName.toString()
            elementName == fieldName || elementName == "$fieldName\$delegate"
        } as? VariableElement
    }

    private fun resolveKtField(kmProperty: KmProperty): IFiled {
        val resolver = KmPropertyResolver(
            processingEnvironment,
            belongingClass,
            findAptVariableElement(kmProperty.name),
            kmProperty
        ).resolveKmProperty()

        logger.i("---| resolveKtField：${resolver}")

        return resolver
    }

    private fun resolveKtField(kmValueParameter: KmValueParameter): IFiled {
        val resolver = KmValueParameterResolver(
            processingEnvironment,
            belongingClass,
            findAptVariableElement(kmValueParameter.name),
            kmValueParameter,
            logger
        ).resolveKmValueParameter()

        logger.i("---| resolveKtField：${resolver}")

        return resolver
    }

    private fun scanBody(): List<IFiled> {
        val constructorFieldNames = kmClass.constructors.map { kmConstructor ->
            kmConstructor.valueParameters.map { it.name }
        }.flatten().distinct()
        val list = kmClass.properties.asSequence().filter {
            it.name !in constructorFieldNames
        }.filter {
            val aptVariableElement = findAptVariableElement(it.name)
            if (cKind == ClassKind.INTERFACE) {
                true
            } else {
                aptVariableElement != null && !aptVariableElement.modifiers.contains(Modifier.TRANSIENT)
            }
        }.map {
            resolveKtField(it)
        }.toList()

        logger.i("---| scanBody：${list}")

        return list
    }

    private fun scanSupers(): List<IFiled> {
        val list = kmClass.supertypes.asSequence().mapNotNull {
            val classifier = it.classifier as? KmClassifier.Class
            if (classifier == null) {
                logger.e("Unexpected super type ${belongingClass.qualifiedName}", belongingClass)
                throw IllegalStateException(
                    "Unexpected super type ${belongingClass.qualifiedName}"
                )
            }
            classifier.name.replace("/", ".")
        }.filter {
            it != Any::class.qualifiedName
        }.map { name ->
            val typeElement = processingEnvironment.elementUtils.getTypeElement(name)
            if (typeElement == null) {
                logger.e("Unexpected super type ${belongingClass.qualifiedName}", belongingClass)
                throw IllegalStateException(
                    "Unexpected super type ${belongingClass.qualifiedName}"
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
            val superTypeElement = classScanner.belongingClass
            val declarationScope = if (superTypeElement.kind == ElementKind.CLASS) {
                DeclarationScope.SUPER_CLASS
            } else {
                DeclarationScope.SUPER_INTERFACE
            }
            classScanner.field.map { it.copy(declarationScope = declarationScope) }
        }.flatten().toList()

        logger.i("---| scanSupers：${list}")

        return list
    }

}