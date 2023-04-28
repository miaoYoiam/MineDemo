package com.example.mine.lib_gson_adapter.typeadapter

import com.example.mine.lib_gson_adapter.base.ElementType
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.squareup.kotlinpoet.TypeSpec
import java.util.logging.Logger

/**
 * [TypeAdapterFactory]类生成器
 */
interface TypeAdapterFactoryGenerator {

    /**
     * 根据数据类与[TypeAdapter]之间的映射关系集合[classToTypeAdapters]生成一个[TypeAdapterFactory]
     *
     * @param typeAdapterFactoryName 生成类的权限定名
     * @param classToTypeAdapters 数据类与[TypeAdapter]之间的映射关系集合
     */
    fun generate(
        typeAdapterFactoryName: String,
        classToTypeAdapters: Set<Pair<ElementType, ElementType>>
    ): TypeSpec

    interface Factory {

        fun create(logger: Logger): TypeAdapterFactoryGenerator
    }
}