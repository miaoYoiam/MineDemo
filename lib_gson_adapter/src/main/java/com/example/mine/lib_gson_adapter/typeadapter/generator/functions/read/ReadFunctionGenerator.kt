package com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read

import com.example.mine.lib_gson_adapter.*
import com.example.mine.lib_gson_adapter.base.ClassScanner
import com.example.mine.lib_gson_adapter.base.DeclarationScope
import com.example.mine.lib_gson_adapter.base.asTypeName
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.strategy.KtTypeReadCodeGeneratorImpl
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier

internal class ReadFunctionGenerator(private val logger: Logger) {

    fun generate(
        scanner: ClassScanner,
        config: TypeAdapterClassGenConfig
    ): FunSpec {
        val className = scanner.classType.asTypeName() as ClassName
        // 1. 生成方法签名
        val readFunc = FunSpec.builder("read")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter(READER, JsonReader::class)
            .returns(className.copy(nullable = true))

        readFunc.beginControlFlow("if ($READER.peek() == %T.NULL)", JsonToken::class)
        readFunc.addStatement("$READER.nextNull()")
        readFunc.addStatement("return null")
        readFunc.endControlFlow()

        readFunc.addStatement("val $DEFAULT_VALUE = %T()", className)

        // 2. 生成每个字段的临时变量
        scanner.field.filter {
            it.declarationScope != DeclarationScope.SUPER_INTERFACE
        }.forEach {
            val fieldName = it.fieldName
            readFunc.addStatement("var $fieldName = $DEFAULT_VALUE.$fieldName")
        }

        // 3. 生成读取json字符串的逻辑
        readFunc.addStatement("$READER.beginObject()")

        readFunc.beginControlFlow("while ($READER.hasNext())")

        readFunc.beginControlFlow("when ($READER.nextName())")
        scanner.field.filter {
            it.declarationScope != DeclarationScope.SUPER_INTERFACE
        }.forEach {
            val conditionCodeBlock = CodeBlock.Builder()
            val keysFormat = it.keys.joinToString(separator = ", ") { "%S" }
            conditionCodeBlock.beginControlFlow("$keysFormat ->", *it.keys.toTypedArray())
            val readCode =
                KtTypeReadCodeGeneratorImpl(
                    logger,
                    config
                ).generate(it.type) { generatingCodeBlock, tempFieldName ->
                    generatingCodeBlock.addStatement("${it.fieldName} = $tempFieldName")
                }
            conditionCodeBlock.add(readCode)
            conditionCodeBlock.endControlFlow()
            readFunc.addCode(conditionCodeBlock.build())
        }
        readFunc.addStatement("else -> $READER.skipValue()")
        readFunc.endControlFlow()

        readFunc.endControlFlow()

        readFunc.addStatement("$READER.endObject()")

        // 4. 生成return语句
        val returnStatement = CodeBlock.Builder()
        val constructorInitializeParams = scanner.field.filter {
            it.declarationScope == DeclarationScope.PRIMARY_CONSTRUCTOR
        }.map {
            val fieldName = it.fieldName
            val tempReadFieldName = it.fieldName
            "$fieldName = $tempReadFieldName"
        }.joinToString(separator = ", ") { it }
        returnStatement.addStatement(
            "val $RETURN_VALUE = %T($constructorInitializeParams)",
            className
        )

        scanner.field.filter { it.declarationScope != DeclarationScope.SUPER_INTERFACE }
            .filterNot {
                it.declarationScope == DeclarationScope.PRIMARY_CONSTRUCTOR
            }.forEach {
            val fieldName = it.fieldName
            val tempReadFieldName = it.fieldName
            returnStatement.addStatement("$RETURN_VALUE.$fieldName = $tempReadFieldName")
        }

        returnStatement.addStatement("return $RETURN_VALUE")

        readFunc.addCode(returnStatement.build())

        return readFunc.build()
    }
}