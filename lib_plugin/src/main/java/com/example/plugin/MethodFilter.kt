package com.example.plugin

/**
 * Create by cxzheng on 2019/8/28
 */
class MethodFilter {

    companion object {

        fun isConstructor(methodName: String?): Boolean {
            return methodName?.contains("<init>") ?: false
        }

    }
}