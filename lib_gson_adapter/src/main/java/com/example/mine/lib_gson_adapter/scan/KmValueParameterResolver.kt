package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.base.DeclarationScope
import com.example.mine.lib_gson_adapter.base.FieldInitializer
import com.example.mine.lib_gson_adapter.base.IFiled
import com.example.mine.lib_gson_adapter.base.IType
import com.google.gson.annotations.SerializedName
import kotlinx.metadata.Flag
import kotlinx.metadata.KmValueParameter
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class KmValueParameterResolver(
    private val processingEnvironment: ProcessingEnvironment,
    private val belongingClass: TypeElement,
    private val aptVariableElement: VariableElement?,
    private val kmValueParameter: KmValueParameter,
    private val logger: Logger
) {

    fun resolveKmValueParameter(): IFiled {
        val fieldName = kmValueParameter.name
        val isFinal = aptVariableElement?.modifiers?.contains(Modifier.FINAL) == true
        val serializedNameAnnotation = aptVariableElement?.getAnnotation(SerializedName::class.java)
        //寻找 SerializedName 注解
        val keys = if (serializedNameAnnotation != null) {
            val result = mutableListOf<String>()
            result.add(serializedNameAnnotation.value)
            result.addAll(serializedNameAnnotation.alternate)
            result.toList()
        } else {
            listOf(fieldName)
        }
        //是否有默认值
        val initializer = if (Flag.ValueParameter.DECLARES_DEFAULT_VALUE(kmValueParameter.flags)) {
            FieldInitializer.DEFAULT
        } else {
            FieldInitializer.NONE
        }
        //获取元素类型
        val ktType = resolveKtType()
        return KaptKtField(
            isFinal = isFinal,
            fieldName = fieldName,
            keys = keys,
            type = ktType,
            initializer = initializer,
            transient = aptVariableElement?.modifiers?.contains(Modifier.TRANSIENT) == true,
            declarationScope = DeclarationScope.PRIMARY_CONSTRUCTOR,
            target = aptVariableElement
        )
    }

    private fun resolveKtType(): IType {
        val kmType = kmValueParameter.type ?: kotlin.run {
            logger.e(
                "Unexpected constructor param type",
                aptVariableElement
            )
            throw IllegalStateException("Unexpected constructor param type ${belongingClass.qualifiedName}.${aptVariableElement?.simpleName}")
        }
        return KmTypeResolver(
            processingEnvironment,
            aptVariableElement,
            kmType
        ).resolvedKtType
    }
}