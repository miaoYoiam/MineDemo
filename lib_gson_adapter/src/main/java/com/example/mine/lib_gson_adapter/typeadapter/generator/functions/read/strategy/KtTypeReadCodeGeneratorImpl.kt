package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy.base.KtTypeReadCodeGenerator
import com.squareup.kotlinpoet.CodeBlock

internal class KtTypeReadCodeGeneratorImpl(
    private val logger: Logger,
    private val config: TypeAdapterClassGenConfig
) : KtTypeReadCodeGenerator {

    override fun generate(
        ktType: ElementType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ): CodeBlock {
        return when {
            ktType.jsonTokenName.isPrimitive() -> {
                PrimitiveKtTypeReadCodeGenerator(logger, config).generate(ktType, codegenHook)
            }
            ktType.jsonTokenName.isObject() -> {
                ObjectKtTypeReadCodeGenerator(logger, config).generate(ktType, codegenHook)
            }
            ktType.jsonTokenName.isArray() -> {
                CollectionKtTypeReadCodeGenerator(logger, config).generate(ktType, codegenHook)
            }
            ktType.jsonTokenName.isMap() -> {
                MapKtTypeReadCodeGenerator(logger, config).generate(ktType, codegenHook)
            }
            ktType.jsonTokenName.isEnum() -> {
                EnumKtTypeReadCodeGenerator(logger, config).generate(ktType, codegenHook)
            }
            else -> throw IllegalStateException("Unexpected json token ${ktType.jsonTokenName}")
        }
    }
}