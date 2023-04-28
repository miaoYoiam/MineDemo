package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.base.DeclarationScope
import com.example.mine.lib_gson_adapter.base.FieldInitializer
import com.example.mine.lib_gson_adapter.base.IFiled
import com.example.mine.lib_gson_adapter.base.IType
import com.google.gson.annotations.SerializedName
import kotlinx.metadata.Flag
import kotlinx.metadata.KmProperty
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class KmPropertyResolver(
    private val processingEnvironment: ProcessingEnvironment,
    private val belongingClass: TypeElement,
    private val aptVariableElement: VariableElement?,
    private val kmProperty: KmProperty
) {

    fun resolveKmProperty(): IFiled {
        val fieldName = kmProperty.name
        val isFinal = !Flag.Property.IS_VAR(kmProperty.flags)
        val serializedNameAnnotation = aptVariableElement?.getAnnotation(SerializedName::class.java)
        val keys = if (serializedNameAnnotation != null) {
            val result = mutableListOf<String>()
            result.add(serializedNameAnnotation.value)
            result.addAll(serializedNameAnnotation.alternate)
            result.toList()
        } else {
            listOf(fieldName)
        }
        val initializer = if (Flag.Property.IS_DELEGATED(kmProperty.flags)) {
            FieldInitializer.DELEGATED
        } else {
            FieldInitializer.DEFAULT
        }
        val ktType = resolveKtType()
        return KaptKtField(
            isFinal = isFinal,
            fieldName = fieldName,
            keys = keys,
            type = ktType,
            initializer = initializer,
            transient = aptVariableElement?.modifiers?.contains(Modifier.TRANSIENT) == true,
            declarationScope = DeclarationScope.BODY,
            target = aptVariableElement
        )
    }

    private fun resolveKtType(): IType {
        return KmTypeResolver(
            processingEnvironment,
            aptVariableElement,
            kmProperty.returnType
        ).resolvedKtType
    }
}