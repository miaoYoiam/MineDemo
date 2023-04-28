package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.WRITER
import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.typeadapter.firstCharLowerCase
import com.example.mine.lib_gson_adapter.typeadapter.flatten
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.base.AbstractKtTypeWriteCodeGenerator
import com.squareup.kotlinpoet.CodeBlock

internal class CollectionKtTypeWriteCodeGenerator(
    logger: Logger,
    config: TypeAdapterClassGenConfig
) : AbstractKtTypeWriteCodeGenerator(logger, config) {
    override fun realGenerate(
        fieldName: String,
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        tempFieldName: String
    ) {
        val generic = ktType.generics.first()

        codeBlockBuilder.addStatement("$WRITER.beginArray()")

        val loopFieldName = generic.flatten().firstCharLowerCase()
        codeBlockBuilder.beginControlFlow("for ($loopFieldName in $tempFieldName)")
        val writeCodeGenerator = KtTypeWriteCodeGeneratorImpl(logger, config)
        val genericCodeBlock = writeCodeGenerator.generate(
            fieldName,
            generic
        ) { genericCodeBlockBuilder, genericTempFieldName ->
            genericCodeBlockBuilder.addStatement("val $genericTempFieldName = $loopFieldName")
        }
        codeBlockBuilder.add(genericCodeBlock)
        codeBlockBuilder.endControlFlow()
        codeBlockBuilder.addStatement("$WRITER.endArray()")
    }
}