package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy


import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.READER
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.base.JsonTokenDelegate
import com.example.mine.lib_gson_adapter.base.asTypeName
import com.example.mine.lib_gson_adapter.base.kotlinPrimitiveType
import com.example.mine.lib_gson_adapter.typeadapter.firstChatUpperCase
import com.example.mine.lib_gson_adapter.typeadapter.flatten
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy.base.AbstractKtTypeReadCodeGenerator
import com.example.mine.lib_gson_adapter.typeadapter.getReadingTempFieldName
import com.squareup.kotlinpoet.CodeBlock

internal class MapKtTypeReadCodeGenerator(
    logger: Logger,
    config: TypeAdapterClassGenConfig
) : AbstractKtTypeReadCodeGenerator(logger, config) {
    override fun enterExpectTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: ElementType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ) {
        val tempFieldName = ktType.getReadingTempFieldName()
        val initializer = getMapInitializer(ktType)
        val keyGeneric = ktType.generics.first()
        val valueGeneric = ktType.generics[1]
        codeBlockBuilder.addStatement(
            "val $tempFieldName = $initializer<%T, %T>()",
            keyGeneric.asTypeName(ignoreVariance = true).kotlinPrimitiveType(),
            valueGeneric.asTypeName(ignoreVariance = true).kotlinPrimitiveType()
        )

        codeBlockBuilder.addStatement("$READER.beginObject()")

        codeBlockBuilder.beginControlFlow("while ($READER.hasNext())")

        val keyFieldName = "keyOf" + valueGeneric.flatten().firstChatUpperCase()
        codeBlockBuilder.addStatement("val $keyFieldName = $READER.nextName()")
        val genericReadCodeGenerator = KtTypeReadCodeGeneratorImpl(logger, config)
        val genericReadCodeBlock =
            genericReadCodeGenerator.generate(valueGeneric) { genericCodeBlockBuilder, genericTempFieldName ->
                genericCodeBlockBuilder.addStatement("$tempFieldName.put($keyFieldName, $genericTempFieldName)")
            }
        codeBlockBuilder.add(genericReadCodeBlock)

        codeBlockBuilder.endControlFlow()

        codeBlockBuilder.addStatement("$READER.endObject()")
        codegenHook.invoke(codeBlockBuilder, tempFieldName)
    }

    override fun enterNullTokenBlock(
        codeBlockBuilder: CodeBlock.Builder,
        ktType: ElementType,
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
        ktType: ElementType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ) {
        generateExpectTokenButTokenBlock(codeBlockBuilder, ktType)
    }

    private fun getMapInitializer(ktType: ElementType): String {
        return when (ktType.jsonTokenName) {
            JsonTokenDelegate.MAP -> {
                "mutableMapOf"
            }
            JsonTokenDelegate.JAVA_MAP -> {
                "java.util.LinkedHashMap"
            }
            else -> {
                throw IllegalStateException("Unexpected json token ${ktType.jsonTokenName}")
            }
        }
    }
}