package com.example.mine.lib_gson_adapter.scan.resolver

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.base.DeclarationScope
import com.example.mine.lib_gson_adapter.base.FieldInitializer
import com.example.mine.lib_gson_adapter.base.ElementFiled
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.modifierFinal
import com.example.mine.lib_gson_adapter.modifierTransient
import com.example.mine.lib_gson_adapter.scan.KtValueField
import com.google.gson.annotations.SerializedName
import kotlinx.metadata.Flag
import kotlinx.metadata.KmValueParameter
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class KmValueParameterResolver(
    private val processingEnvironment: ProcessingEnvironment,
    private val targetClass: TypeElement,
    private val variableElement: VariableElement?,
    private val kmValueParameter: KmValueParameter,
    private val logger: Logger
) {

    fun resolveKmValueParameter(): ElementFiled {
        val fieldName = kmValueParameter.name
        val isFinal = variableElement?.modifierFinal() == true

        val serializedNameAnnotation = variableElement?.getAnnotation(SerializedName::class.java)

        /**
         * 寻找 SerializedName 注解
         *
         * value：键名
         * alternate：键别名
         */
        val keys = if (serializedNameAnnotation != null) {
            val result = mutableListOf<String>()
            result.add(serializedNameAnnotation.value)
            result.addAll(serializedNameAnnotation.alternate)
            result.toList()
        } else {
            listOf(fieldName)
        }

        /**
         * Flag.ValueParameter.IS_VARARG ：可变参数
         * Flag.ValueParameter.DECLARES_DEFAULT_VALUE： 是否有默认值
         */
        val initializer = if (Flag.ValueParameter.DECLARES_DEFAULT_VALUE(kmValueParameter.flags)) {
            FieldInitializer.DEFAULT
        } else {
            FieldInitializer.NONE
        }
        //获取元素类型
        val ktType = resolveKtType()
        return KtValueField(
            isFinal = isFinal,
            fieldName = fieldName,
            keys = keys,
            type = ktType,
            initializer = initializer,
            transient = variableElement?.modifierTransient() == true,
            declarationScope = DeclarationScope.PRIMARY_CONSTRUCTOR,
            target = variableElement
        )
    }

    private fun resolveKtType(): ElementType {
        val kmType = kmValueParameter.type ?: kotlin.run {
            logger.e("Unexpected constructor param type", variableElement)
            throw IllegalStateException("Unexpected constructor param type ${targetClass.qualifiedName}.${variableElement?.simpleName}")
        }
        logger.i("---|---| resolveKtType kmType ${targetClass.qualifiedName}.${variableElement?.simpleName}")
        return KmTypeResolver(
            processingEnvironment,
            variableElement,
            kmType,
            logger
        ).resolvedKtType
    }
}