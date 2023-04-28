package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.base.KtTypeWriteCodeGenerator

import com.squareup.kotlinpoet.CodeBlock

internal class KtTypeWriteCodeGeneratorImpl(
    private val logger: Logger,
    private val config: TypeAdapterClassGenConfig
) : KtTypeWriteCodeGenerator {
    override fun generate(
        fieldName: String,
        ktType: ElementType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ): CodeBlock {
        return when {
            ktType.jsonTokenName.isPrimitive() -> {
                PrimitiveKtTypeWriteCodeGenerator(logger, config).generate(
                    fieldName,
                    ktType,
                    codegenHook
                )
            }
            ktType.jsonTokenName.isObject() -> {
                ObjectKtTypeWriteCodeGenerator(logger, config).generate(
                    fieldName,
                    ktType,
                    codegenHook
                )
            }
            ktType.jsonTokenName.isEnum() -> {
                EnumKtTypeWriteCodeGenerator(logger, config).generate(
                    fieldName,
                    ktType,
                    codegenHook
                )
            }
            ktType.jsonTokenName.isArray() -> {
                CollectionKtTypeWriteCodeGenerator(logger, config).generate(
                    fieldName,
                    ktType,
                    codegenHook
                )
            }
            ktType.jsonTokenName.isMap() -> {
                MapKtTypeWriteCodeGenerator(logger, config).generate(fieldName, ktType, codegenHook)
            }
            else -> throw IllegalStateException("Unexpected json token ${ktType.jsonTokenName}")
        }
    }
}