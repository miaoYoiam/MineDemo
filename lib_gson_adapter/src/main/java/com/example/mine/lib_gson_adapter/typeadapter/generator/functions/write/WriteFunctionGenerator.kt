package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write

import com.google.gson.stream.JsonWriter

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.OBJECT
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.WRITER
import com.example.mine.lib_gson_adapter.base.ClassScanner
import com.example.mine.lib_gson_adapter.base.DeclarationScope
import com.example.mine.lib_gson_adapter.base.asTypeName
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.strategy.KtTypeWriteCodeGeneratorImpl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier

internal class WriteFunctionGenerator(private val logger: Logger) {

    fun generate(
        scanner: ClassScanner,
        config: TypeAdapterClassGenConfig
    ): FunSpec {
        val className = scanner.classType.asTypeName() as ClassName
        val writeFunc = FunSpec.builder("write")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(WRITER, JsonWriter::class)
            .addParameter(OBJECT, className.copy(nullable = true))

        writeFunc.beginControlFlow("if ($OBJECT == null)")
        writeFunc.addStatement("$WRITER.nullValue()")
        writeFunc.addStatement("return")
        writeFunc.endControlFlow()

        writeFunc.addStatement("$WRITER.beginObject()")
        scanner.field.filter { it.declarationScope != DeclarationScope.SUPER_INTERFACE }
            .forEach {
                val writeCodeGenerator = KtTypeWriteCodeGeneratorImpl(logger, config)
                val codeBlock = writeCodeGenerator.generate(
                    it.fieldName,
                    it.type
                ) { tempCodeBlock, tempFieldName ->
                    tempCodeBlock.addStatement("$WRITER.name(%S)", it.keys.first())
                    tempCodeBlock.addStatement("val $tempFieldName = $OBJECT.${it.fieldName}")
                }
                writeFunc.addCode(codeBlock)
            }
        writeFunc.addStatement("$WRITER.endObject()")

        return writeFunc.build()
    }
}