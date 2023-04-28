package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy

import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.WRITER
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.typeadapter.firstChatUpperCase
import com.example.mine.lib_gson_adapter.typeadapter.flatten
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.base.AbstractKtTypeWriteCodeGenerator

import com.squareup.kotlinpoet.CodeBlock

internal class MapKtTypeWriteCodeGenerator(
    logger: Logger,
    config: TypeAdapterClassGenConfig
) : AbstractKtTypeWriteCodeGenerator(logger, config) {
    override fun realGenerate(
        fieldName: String,
        codeBlockBuilder: CodeBlock.Builder,
        ktType: ElementType,
        tempFieldName: String
    ) {
        val valueGeneric = ktType.generics[1]

        codeBlockBuilder.addStatement("$WRITER.beginObject()")

        val loopKeyFieldName = "key" + valueGeneric.flatten().firstChatUpperCase()
        val loopValueFieldName = "value" + valueGeneric.flatten().firstChatUpperCase()
        codeBlockBuilder.beginControlFlow("for (($loopKeyFieldName, $loopValueFieldName) in $tempFieldName)")

        val writeCodeGenerator = KtTypeWriteCodeGeneratorImpl(logger, config)
        val genericCodeBlock = writeCodeGenerator.generate(
            fieldName,
            valueGeneric
        ) { genericCodeBlockBuilder, genericTempFieldName ->
            genericCodeBlockBuilder.addStatement("$WRITER.name($loopKeyFieldName)")
            genericCodeBlockBuilder.addStatement("val $genericTempFieldName = $loopValueFieldName")
        }
        codeBlockBuilder.add(genericCodeBlock)

        codeBlockBuilder.endControlFlow()

        codeBlockBuilder.addStatement("$WRITER.endObject()")
    }
}