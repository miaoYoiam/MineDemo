package com.example.plugin

/**
 * Create by cxzheng on 2019/8/28
 */
object MethodFilter {
    fun isConstructor(methodName: String?): Boolean {
        return methodName?.contains("<init>") ?: false
    }
}