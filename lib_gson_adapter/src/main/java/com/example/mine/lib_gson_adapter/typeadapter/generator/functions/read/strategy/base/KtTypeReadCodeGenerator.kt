package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy.base

import com.example.mine.lib_gson_adapter.base.ElementType
import com.squareup.kotlinpoet.CodeBlock

interface KtTypeReadCodeGenerator {

    fun generate(
        ktType: ElementType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ): CodeBlock
}