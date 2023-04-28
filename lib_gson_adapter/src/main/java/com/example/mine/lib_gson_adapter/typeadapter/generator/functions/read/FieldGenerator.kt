package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read

import com.example.mine.lib_gson_adapter.GSON
import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.base.ClassScanner
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.base.asTypeName
import com.example.mine.lib_gson_adapter.typeadapter.getTypeAdapterClassName
import com.example.mine.lib_gson_adapter.typeadapter.getTypeAdapterFieldName
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.squareup.kotlinpoet.*

internal class FieldGenerator(private val logger: Logger) {

    private lateinit var classFilterMap: Map<String, ElementType>

    fun setClassFilter(classFilterMap: Map<String, ElementType>) {
        this.classFilterMap = classFilterMap
    }

    fun generateByKtType(scanner: ClassScanner, ktType: ElementType): PropertySpec {
        val isRegisteredType = classFilterMap.containsKey(ktType.rawType) && ktType.generics.isEmpty()
        val adapterFieldName = ktType.getTypeAdapterFieldName()
        val typeName = ktType.asTypeName(ignoreVariance = true, ignoreNullability = true)
        val typeAdapterCodeBlock = if (isRegisteredType) {
            // 已经注册的，使用XXXTypeAdapter()
            val build = CodeBlock.Builder()
                .beginControlFlow("lazy")
                .addStatement("%T($GSON)", ktType.getTypeAdapterClassName())
                .endControlFlow()
                .build()
            build
        } else {
            // 没有注册的，使用gson.getAdapter(XXX::class.java)
            CodeBlock.Builder()
                .beginControlFlow("lazy")
                .addStatement("$GSON.getAdapter(object : %T<%T>() {})", TypeToken::class, typeName)
                .endControlFlow()
                .build()
        }
        val adapterType = with(ParameterizedTypeName.Companion) {
            TypeAdapter::class.asClassName().parameterizedBy(typeName)
        }
        logger.d("generate field $adapterFieldName for class ${scanner.classType.asTypeName()}")
        return PropertySpec.builder(adapterFieldName, adapterType, KModifier.PRIVATE)
            .delegate(typeAdapterCodeBlock)
            .build()
    }
}