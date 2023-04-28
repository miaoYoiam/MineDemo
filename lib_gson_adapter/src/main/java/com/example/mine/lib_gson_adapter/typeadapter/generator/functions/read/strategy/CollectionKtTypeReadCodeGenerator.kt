package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy

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

internal class CollectionKtTypeReadCodeGenerator(
    logger: Logger,
    config: TypeAdapterClassGenConfig
) : AbstractKtTypeReadCodeGenerator(logger, config) {

    override fun enterExpectTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ) {
        val tempFieldName = ktType.getReadingTempFieldName()
        val initializer = getCollectionInitializer(ktType)
        val generic = ktType.generics.first()
        codeBlockBuilder.addStatement(
            "val $tempFieldName = $initializer<%T>()",
            generic.asTypeName(ignoreVariance = true).kotlinPrimitiveType()
        )

        codeBlockBuilder.addStatement("$READER.beginArray()")
        codeBlockBuilder.beginControlFlow("while ($READER.hasNext())")

        val genericReadCodeGenerator = KtTypeReadCodeGeneratorImpl(logger, config)
        val genericReadCodeBlock =
            genericReadCodeGenerator.generate(generic) { genericCodeBlockBuilder, genericTempFieldName ->
                genericCodeBlockBuilder.addStatement("$tempFieldName.add($genericTempFieldName)")
            }
        codeBlockBuilder.add(genericReadCodeBlock)
        codeBlockBuilder.endControlFlow()
        codeBlockBuilder.addStatement("$READER.endArray()")
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
                codegenHook.invoke(codeBlockBuilder, "null")
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

    private fun getCollectionInitializer(ktType: IType): String {
        return when (val jsonTokenName = ktType.jsonTokenName) {
            JsonTokenDelegate.LIST -> "mutableListOf"
            JsonTokenDelegate.SET -> "mutableSetOf"
            JsonTokenDelegate.JAVA_LIST -> "java.util.ArrayList"
            JsonTokenDelegate.JAVA_SET -> "java.util.LinkedHashSet"
            else -> throw IllegalStateException("Unexpected json token $jsonTokenName")
        }
    }
}