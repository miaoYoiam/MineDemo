package com.example.mine.lib_gson_adapter.typeadapter

import com.example.mine.lib_gson_adapter.GSON
import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TYPE_TOKEN
import com.example.mine.lib_gson_adapter.base.ElementType
import com.example.mine.lib_gson_adapter.base.asTypeName
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

internal class TypeAdapterFactoryClassGeneratorImpl(
    private val logger: Logger
) : TypeAdapterFactoryGenerator {
    override fun generate(
        typeAdapterFactoryName: String,
        classToTypeAdapters: Set<Pair<ElementType, ElementType>>
    ): TypeSpec {
        val typeAdapterFactoryClassName = ClassName.bestGuess(typeAdapterFactoryName)
        return TypeSpec
            .classBuilder(typeAdapterFactoryClassName)
            .addSuperinterface(TypeAdapterFactory::class)
            .addFunction(generateCreateFunc(classToTypeAdapters))
            .build()
    }

    private fun generateCreateFunc(classToTypeAdapters: Set<Pair<ElementType, ElementType>>): FunSpec {
        val returnType = TypeAdapter::class.asClassName()
            .parameterizedBy(TypeVariableName.invoke("T")).copy(nullable = true)

        val createFun = FunSpec.builder("create")
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("%S", "UNCHECKED_CAST")
                    .build()
            )
            .addModifiers(KModifier.OVERRIDE)
            .addTypeVariable(TypeVariableName.invoke("T"))
            .addParameter(GSON, Gson::class)
            .addParameter(
                TYPE_TOKEN,
                TypeToken::class.asTypeName().parameterizedBy(TypeVariableName.invoke("T"))
            ).returns(returnType)

        val codeBlock = CodeBlock.Builder()
        codeBlock.beginControlFlow("val typeAdapter = when")
        classToTypeAdapters.forEach { (clazz, typeAdapterClazz) ->
            codeBlock.addStatement(
                "%T::class.java.isAssignableFrom(%L.rawType) -> %T(%L)",
                clazz.asTypeName(),
                TYPE_TOKEN,
                typeAdapterClazz.asTypeName(),
                GSON
            )
        }

        codeBlock.addStatement("else -> null")
        codeBlock.endControlFlow()
        codeBlock.addStatement("return typeAdapter as? %T", returnType)
        createFun.addCode(codeBlock.build())
        return createFun.build()
    }
}