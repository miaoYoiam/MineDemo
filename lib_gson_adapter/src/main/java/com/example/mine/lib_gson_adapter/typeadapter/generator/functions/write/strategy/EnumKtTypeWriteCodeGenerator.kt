package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.WRITER
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.base.AbstractKtTypeWriteCodeGenerator
import com.squareup.kotlinpoet.CodeBlock

internal class EnumKtTypeWriteCodeGenerator(
    logger: Logger,
    config: TypeAdapterClassGenConfig
) : AbstractKtTypeWriteCodeGenerator(logger, config) {
    override fun realGenerate(
        fieldName: String,
        codeBlockBuilder: CodeBlock.Builder,
        ktType: ElementType,
        tempFieldName: String
    ) {
        codeBlockBuilder.addStatement("$WRITER.value($tempFieldName.name)")
    }
}