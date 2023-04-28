package com.example.mine.lib_gson_adapter.typeadapter

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.TypeAdapterClassGenConfig
import com.example.mine.lib_gson_adapter.base.ClassScanner
import com.example.mine.lib_gson_adapter.base.ElementType
import com.google.gson.TypeAdapter
import com.squareup.kotlinpoet.TypeSpec

/**
 * [TypeAdapter]类生成器
 */
interface TypeAdapterGenerator {

    /**
     * 根据扫描结果[scanner]，生成一个TypeAdapter
     *
     * @param scanner 类扫描器
     * @param config 逻辑代码生成上的配置
     */
    fun generate(
        scanner: ClassScanner,
        classFilter: Set<ElementType>,
        config: TypeAdapterClassGenConfig = TypeAdapterClassGenConfig()
    ): TypeSpec

    interface Factory {
        fun create(logger: Logger): TypeAdapterGenerator
    }
}