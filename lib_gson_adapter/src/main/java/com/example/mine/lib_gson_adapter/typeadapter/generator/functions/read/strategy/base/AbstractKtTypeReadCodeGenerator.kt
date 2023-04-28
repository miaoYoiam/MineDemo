package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy.base

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.READER
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.typeadapter.getPeekedFieldName
import com.google.gson.stream.JsonToken
import com.squareup.kotlinpoet.CodeBlock

internal abstract class AbstractKtTypeReadCodeGenerator(
    protected val logger: Logger,
    protected val config: TypeAdapterClassGenConfig
) : KtTypeReadCodeGenerator {

    protected val nullSafe: Boolean = config.nullSafe

    protected val strictType: Boolean = config.strictType

    final override fun generate(
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ): CodeBlock {
        val codeBlockBuilder = CodeBlock.Builder()

        val peekedFieldName = ktType.getPeekedFieldName()
        codeBlockBuilder.beginControlFlow("when (val $peekedFieldName = $READER.peek())")

        codeBlockBuilder.beginControlFlow(
            "%T.${ktType.jsonTokenName.jsonToken} -> ",
            JsonToken::class
        )
        enterExpectTokenBlock(codeBlockBuilder, ktType, codegenHook)
        codeBlockBuilder.endControlFlow()

        codeBlockBuilder.beginControlFlow("%T.NULL ->", JsonToken::class)
        enterNullTokenBlock(codeBlockBuilder, ktType, codegenHook)
        codeBlockBuilder.endControlFlow()

        codeBlockBuilder.beginControlFlow("else -> ")
        enterOtherTokenBlock(codeBlockBuilder, ktType, codegenHook)
        codeBlockBuilder.endControlFlow()

        codeBlockBuilder.endControlFlow()

        return codeBlockBuilder.build()
    }

    abstract fun enterExpectTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    )

    abstract fun enterNullTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    )

    abstract fun enterOtherTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    )

    protected fun generateExpectTokenButTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType
    ) {
        val peekedFieldName = ktType.getPeekedFieldName()
        codeBlockBuilder.addStatement(
            "throw %T(%P)",
            IllegalStateException::class,
            "Expect ${ktType.jsonTokenName.jsonToken} but was $$peekedFieldName"
        )
    }
}