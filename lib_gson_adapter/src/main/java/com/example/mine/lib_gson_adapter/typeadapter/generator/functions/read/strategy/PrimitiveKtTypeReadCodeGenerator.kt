package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy

import com.google.gson.internal.bind.TypeAdapters
import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.READER
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.base.JsonTokenDelegate
import com.example.mine.lib_gson_adapter.base.asTypeName
import com.example.mine.lib_gson_adapter.base.kotlinPrimitiveType
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy.base.AbstractKtTypeReadCodeGenerator
import com.example.mine.lib_gson_adapter.typeadapter.getReadingTempFieldName
import com.squareup.kotlinpoet.CodeBlock

internal class PrimitiveKtTypeReadCodeGenerator(
    logger: Logger,
    config: TypeAdapterClassGenConfig
) : AbstractKtTypeReadCodeGenerator(logger, config) {

    override fun enterExpectTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ) {
        val tempFieldName = ktType.getReadingTempFieldName()
        val nextFuncExp = ktType.jsonTokenName.nextFuncExp
        codeBlockBuilder.addStatement(
            "val $tempFieldName: %T = $READER.$nextFuncExp",
            ktType.asTypeName().kotlinPrimitiveType()
        )
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
                codeBlockBuilder.addStatement(
                    "val $tempFieldName: %T = null",
                    ktType.asTypeName().kotlinPrimitiveType()
                )
                codegenHook.invoke(codeBlockBuilder, tempFieldName)
            }
            nullSafe && !strictType -> {
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
        if (strictType) {
            generateExpectTokenButTokenBlock(codeBlockBuilder, ktType)
        } else {
            val gsonInternalTypeAdapterName = getGsonInternalTypeAdapterName(ktType)
            val tempFieldName = ktType.getReadingTempFieldName()
            codeBlockBuilder.addStatement(
                "val $tempFieldName: %T = %T.%L.read($READER) as %T",
                ktType.asTypeName().kotlinPrimitiveType(),
                TypeAdapters::class,
                gsonInternalTypeAdapterName,
                ktType.asTypeName().kotlinPrimitiveType()
            )
            codegenHook.invoke(codeBlockBuilder, tempFieldName)
        }
    }

    private fun getGsonInternalTypeAdapterName(ktType: IType): String {
        return when (ktType.jsonTokenName) {
            JsonTokenDelegate.INT -> "INTEGER"
            JsonTokenDelegate.LONG -> "LONG"
            JsonTokenDelegate.FLOAT -> "FLOAT"
            JsonTokenDelegate.DOUBLE -> "DOUBLE"
            JsonTokenDelegate.STRING -> "STRING"
            JsonTokenDelegate.BOOLEAN -> "BOOLEAN"
            else -> {
                logger.e("unexpected ktType", ktType)
                throw IllegalArgumentException("unexpected ktType ${ktType.to2String()}")
            }
        }
    }
}