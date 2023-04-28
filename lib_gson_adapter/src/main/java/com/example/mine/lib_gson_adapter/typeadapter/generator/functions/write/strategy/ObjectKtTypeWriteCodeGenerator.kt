package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.WRITER
import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.base.AbstractKtTypeWriteCodeGenerator
import com.example.mine.lib_gson_adapter.typeadapter.getTypeAdapterFieldName

import com.squareup.kotlinpoet.CodeBlock

internal class ObjectKtTypeWriteCodeGenerator(
    logger: Logger,
    config: TypeAdapterClassGenConfig
) : AbstractKtTypeWriteCodeGenerator(logger, config) {

    override fun realGenerate(
        fieldName: String,
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        tempFieldName: String
    ) {
        val typeAdapterFieldName = ktType.getTypeAdapterFieldName()
        codeBlockBuilder.addStatement("$typeAdapterFieldName.write($WRITER, $tempFieldName)")
    }
}