package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.base

import com.example.mine.lib_gson_adapter.base.ElementType
import com.squareup.kotlinpoet.CodeBlock

interface KtTypeWriteCodeGenerator {

    fun generate(
        fieldName: String,
        ktType: ElementType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ): CodeBlock
}