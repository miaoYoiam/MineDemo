package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.base

import com.example.mine.lib_gson_adapter.base.IType
import com.squareup.kotlinpoet.CodeBlock

interface KtTypeWriteCodeGenerator {

    fun generate(
        fieldName: String,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ): CodeBlock
}