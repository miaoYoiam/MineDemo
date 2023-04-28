package com.example.mine.lib_gson_adapter.typeadapter

import com.example.mine.lib_gson_adapter.GSON
import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.ClassScanner
import com.example.mine.lib_gson_adapter.base.IType
import com.example.mine.lib_gson_adapter.base.asTypeName
import com.example.mine.lib_gson_adapter.base.parameterizedBy
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.FieldGenerator
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.read.ReadFunctionGenerator
import com.example.mine.lib_gson_adapter.typeadapter.generator.functions.write.WriteFunctionGenerator
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

internal class TypeAdapterGeneratorImpl(private val logger: Logger) : TypeAdapterGenerator {

    private val fieldGenerator by lazy { FieldGenerator(logger) }

    private val readFunctionGenerator by lazy { ReadFunctionGenerator(logger) }

    private val writeFunctionGenerator by lazy { WriteFunctionGenerator(logger) }

    override fun generate(
        scanner: ClassScanner,
        classFilter: Set<IType>,
        config: TypeAdapterClassGenConfig
    ): TypeSpec {
        preCheckKtType(scanner.classType, "generate() >>>")
        setClassFilter(classFilter)
        val className = scanner.classType
        val typeAdapterClassName = className.getTypeAdapterClassName()
        return TypeSpec.classBuilder(typeAdapterClassName)
            .superclass(TypeAdapter::class.parameterizedBy(className.asTypeName()))
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(GSON, Gson::class)
                    .build()
            ).addProperty(
                PropertySpec.builder(GSON, Gson::class)
                    .initializer(GSON)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            ).addClassBodyFields(scanner, config)
            .addReadFunction(scanner, config)
            .addWriteFunction(scanner, config)
            .build()
    }

    private fun setClassFilter(classes: Set<IType>) {
        val classFilterMap = classes.map {
            preCheckKtType(it, "setClassFilter() >>>")
            it.rawType to it
        }.toMap()
        fieldGenerator.setClassFilter(classFilterMap)
    }

    private fun TypeSpec.Builder.addClassBodyFields(
        scanner: ClassScanner,
        config: TypeAdapterClassGenConfig
    ): TypeSpec.Builder = apply {
        scanner.field.mapNotNull { ktField ->
            ktField.type.dfs { jsonTokenName.isObject() }.firstOrNull()
        }.distinctBy {
            it.rawType
        }.map {
            fieldGenerator.generateByKtType(scanner, it)
        }.forEach {
            addProperty(it)
        }
    }

    private fun TypeSpec.Builder.addReadFunction(
        scanner: ClassScanner,
        config: TypeAdapterClassGenConfig
    ): TypeSpec.Builder = apply {
        addFunction(readFunctionGenerator.generate(scanner, config))
    }

    private fun TypeSpec.Builder.addWriteFunction(
        scanner: ClassScanner,
        config: TypeAdapterClassGenConfig
    ): TypeSpec.Builder = apply {
        addFunction(writeFunctionGenerator.generate(scanner, config))
    }

    private fun preCheckKtType(className: IType, tag: String) {
        if (className.generics.isNotEmpty()) {
            logger.e("$tag illegal className ${className.to2String()}", className)
        }
    }
}