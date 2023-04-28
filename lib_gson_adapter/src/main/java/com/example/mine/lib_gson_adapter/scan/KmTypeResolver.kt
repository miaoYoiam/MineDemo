package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.base.JsonTokenDelegate
import com.example.mine.lib_gson_adapter.base.Variance
import com.example.mine.lib_gson_adapter.base.kotlinType
import com.squareup.kotlinpoet.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.KmClassifier
import kotlinx.metadata.KmType
import kotlinx.metadata.KmVariance
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.VariableElement

/**
 * 元素类型解析器
 */
class KmTypeResolver(
    processingEnvironment: ProcessingEnvironment,
    private val belongingVariant: VariableElement?,
    kmType: KmType
) : AbstractKmTypeResolver(processingEnvironment, kmType) {

    override val resolvedKtType: IType = resolveKtType()

    private fun resolveKtType(): IType {
        return kmType.parseKmType()
    }

    private fun KmType.parseKmType(): IType {
        val kmClassifier = classifier as KmClassifier.Class
        val name = kmClassifier.name.replace("/", ".")
        val nullable = Flag.Type.IS_NULLABLE(flags)
        val jsonTokenName = parseJsonTokenName()
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
            target = belongingVariant
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