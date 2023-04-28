package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy.base

import com.example.mine.lib_gson_adapter.base.IType
import com.squareup.kotlinpoet.CodeBlock

interface KtTypeReadCodeGenerator {

    fun generate(
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ): CodeBlock
}