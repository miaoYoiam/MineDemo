package com.example.mine.lib_gson_adapter.base

/**
 * Day：2023/4/27 15:49
 * @author zhanglei
 */
enum class DeclarationScope {
    PRIMARY_CONSTRUCTOR, //主构造函数
    BODY, //类里面
    SUPER_CLASS, //父类
    SUPER_INTERFACE //接口
}