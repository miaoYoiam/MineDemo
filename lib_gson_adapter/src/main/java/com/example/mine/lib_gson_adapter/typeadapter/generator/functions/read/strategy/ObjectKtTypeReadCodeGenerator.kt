package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy


import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.READER
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.base.asTypeName
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy.base.AbstractKtTypeReadCodeGenerator
import com.example.mine.lib_gson_adapter.typeadapter.getReadingTempFieldName
import com.example.mine.lib_gson_adapter.typeadapter.getTypeAdapterFieldName
import com.squareup.kotlinpoet.CodeBlock

internal class ObjectKtTypeReadCodeGenerator(
    logger: Logger,
    config: TypeAdapterClassGenConfig
) : AbstractKtTypeReadCodeGenerator(logger, config) {

    override fun enterExpectTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ) {
        val tempFieldName = ktType.getReadingTempFieldName()
        val typeAdapterFieldName = ktType.getTypeAdapterFieldName()
        codeBlockBuilder.addStatement("val $tempFieldName = $typeAdapterFieldName.read($READER)")
        codegenHook.invoke(codeBlockBuilder, tempFieldName)
    }

    override fun enterNullTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ) {
        when {
            ktType.nullable -> {
                codeBlockBuilder.addStatement("$READER.nextNull()")

                val tempFieldName = ktType.getReadingTempFieldName()
                codeBlockBuilder.addStatement("val $tempFieldName: %T = null", ktType.asTypeName())
                codegenHook.invoke(codeBlockBuilder, tempFieldName)
            }
            nullSafe -> {
                codeBlockBuilder.addStatement("$READER.skipValue()")
            }
            else -> {
                generateExpectTokenButTokenBlock(codeBlockBuilder, ktType)
            }
        }
    }

    override fun enterOtherTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ) {
        generateExpectTokenButTokenBlock(codeBlockBuilder, ktType)
    }
}