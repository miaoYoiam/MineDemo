package com.example.mine.lib_gson_adapter.base

/**
 * Day：2023/4/27 15:33
 * @author zhanglei
 */
abstract class IType {

    /**
     * Gson.TypeToken.the raw (non-generic) type for this type.
     */
    abstract val rawType: String

    //是否可空
    abstract val nullable: Boolean

    //可变性
    abstract val variance: Variance

    //映射Gson JsonToken
    abstract val jsonTokenName: JsonTokenDelegate

    //泛型
    abstract val generics: List<IType>

    abstract fun copy(
        nullable: Boolean = this.nullable,
        variance: Variance = this.variance
    ): IType

    fun dfs(predicate: IType.() -> Boolean): Set<IType> {
        val result = if (predicate(this)) {
            setOf(this)
        } else {
            setOf()
        }
        return result + generics.map { it.dfs(predicate) }.flatten().toSet()
    }

    fun to2String(): String = buildString {
        if (variance in listOf(Variance.IN, Variance.OUT)) {
            append(variance)
            append(" ")
        }
        append(rawType)
        if (generics.isNotEmpty()) {
            val genericsName = generics.map { it.toString() }.joinToString { it }
            append("<")
            append(genericsName)
            append(">")
        }
        if (nullable) {
            append("?")
        }
        append(" ")
        append(jsonTokenName)
    }
}