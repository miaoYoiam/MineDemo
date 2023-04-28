package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.base


import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.WRITER
import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.typeadapter.getWritingTempFieldName
import com.squareup.kotlinpoet.CodeBlock

internal abstract class AbstractKtTypeWriteCodeGenerator(
    protected val logger: Logger,
    protected val config: TypeAdapterClassGenConfig
) : KtTypeWriteCodeGenerator {

    final override fun generate(
        fieldName: String,
        ktType: IType,
        codegenHook: (CodeBlock.Builder, String) -> Unit
    ): CodeBlock {
        val codeBlockBuilder = CodeBlock.Builder()
        val tempFieldName = ktType.getWritingTempFieldName(fieldName)
        codegenHook.invoke(codeBlockBuilder, tempFieldName)
        if (ktType.nullable) {
            codeBlockBuilder.beginControlFlow("if ($tempFieldName == null)")
            codeBlockBuilder.addStatement("$WRITER.nullValue()")
            codeBlockBuilder.endControlFlow()

            codeBlockBuilder.beginControlFlow("else")
            realGenerate(fieldName, codeBlockBuilder, ktType, tempFieldName)
            codeBlockBuilder.endControlFlow()
        } else {
            realGenerate(fieldName, codeBlockBuilder, ktType, tempFieldName)
        }
        return codeBlockBuilder.build()
    }

    abstract fun realGenerate(
        fieldName: String,
        codeBlockBuilder: CodeBlock.Builder,
        ktType: IType,
        tempFieldName: String
    )
}