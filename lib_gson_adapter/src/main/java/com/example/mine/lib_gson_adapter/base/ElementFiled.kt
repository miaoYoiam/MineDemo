package com.example.mine.lib_gson_adapter.base

/**
 * Day：2023/4/27 15:47
 * @author zhanglei
 */
abstract class ElementFiled {

    /**
     * 是否可变
     */
    abstract val isFinal: Boolean

    /**
     * 变量名
     */
    abstract val fieldName: String

    /**
     * serialize names
     */
    abstract val keys: List<String>

    /**
     * 类型
     */
    abstract val type: ElementType

    /**
     * 初始化器，是否有默认值，或者没有初始化器（包括delegate）
     */
    abstract val initializer: FieldInitializer

    /**
     * 定义处，主构造器、类体或超类型中
     */
    abstract val declarationScope: DeclarationScope

    abstract val transient: Boolean

    /**
     * 复制一个[KtField]
     */
    abstract fun copy(declarationScope: DeclarationScope = this.declarationScope): ElementFiled

    /**
     * 转换成可读的字符串，如:
     * val listInt: kotlin.collections.List<kotlin.Int? INT>? LIST HAS_DEFAULT BODY ["list_int"]
     */
    fun to2String(): String = buildString {
        if (isFinal) {
            append("val ")
        } else {
            append("var ")
        }
        append(fieldName)
        append(" : ")
        append(type.to2String())
        append(" ")
        append(initializer.toString())
        append(" ")
        append(declarationScope.toString())
        append(" keys: ")
        append(keys.toString())
    }


}