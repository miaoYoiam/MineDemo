package com.example.mine.lib_gson_adapter.scan.resolver

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.base.JsonTokenDelegate
import com.example.mine.lib_gson_adapter.base.Variance
import com.example.mine.lib_gson_adapter.base.kotlinType
import com.example.mine.lib_gson_adapter.scan.KaptKtType
import com.squareup.kotlinpoet.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.KmClassifier
import kotlinx.metadata.KmType
import kotlinx.metadata.KmVariance
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.VariableElement

/**
 * 变量类型解析
 */
class KmTypeResolver(
    processingEnvironment: ProcessingEnvironment,
    private val variableElement: VariableElement?,
    kmType: KmType,
    private val logger: Logger? = null
) : AbstractKmTypeResolver(processingEnvironment, kmType) {

    override val resolvedKtType: ElementType = resolveKtType()

    private fun resolveKtType(): ElementType {
        return kmType.parseKmType()
    }

    /**
     * classifier Class(name=kotlin/Int)
     *
     * classifier Class(name=kotlin/String)
     */
    private fun KmType.parseKmType(): ElementType {
        val kmClassifier = classifier as KmClassifier.Class
        val name = kmClassifier.name.replace("/", ".")
        val nullable = Flag.Type.IS_NULLABLE(flags)
        val jsonTokenName = parseJsonTokenName()

        logger?.i("---|---| resolveKtType classifier $classifier")
        logger?.i("---|---| resolveKtType arguments $arguments")
        logger?.i("---|---| resolveKtType jsonTokenName $jsonTokenName")

        val generics = arguments.map {
            val variance = when (it.variance) {
                KmVariance.IN -> Variance.IN
                KmVariance.OUT -> Variance.OUT
                else -> Variance.INVARIANT
            }
            it.type!!.parseKmType().copy(variance = variance)
        }
        return KaptKtType(
            rawType = name,
            nullable = nullable,
            variance = Variance.INVARIANT,
            jsonTokenName = jsonTokenName,
            generics = generics,
            target = variableElement
        )
    }

    private fun KmType.parseJsonTokenName(): JsonTokenDelegate {
        val kmClassifier = classifier as KmClassifier.Class
        val name = kmClassifier.name.replace("/", ".")
        return when {
            isPrimitive(this) -> {
                val kotlinType = ClassName.bestGuess(name).kotlinType() as ClassName
                when (kotlinType.simpleName) {
                    "Int" -> JsonTokenDelegate.INT
                    "Long" -> JsonTokenDelegate.LONG
                    "Float" -> JsonTokenDelegate.FLOAT
                    "Double" -> JsonTokenDelegate.DOUBLE
                    "String" -> JsonTokenDelegate.STRING
                    "Boolean" -> JsonTokenDelegate.BOOLEAN
                    else -> throw IllegalStateException("")
                }
            }
            isKotlinList(this) -> JsonTokenDelegate.LIST
            isJavaList(this) -> JsonTokenDelegate.JAVA_LIST
            isKotlinSet(this) -> JsonTokenDelegate.SET
            isJavaSet(this) -> JsonTokenDelegate.JAVA_SET
            isKotlinMap(this) -> JsonTokenDelegate.MAP
            isJavaMap(this) -> JsonTokenDelegate.JAVA_MAP
            isEnum(this) -> JsonTokenDelegate.ENUM
            else -> JsonTokenDelegate.OBJECT
        }
    }
}