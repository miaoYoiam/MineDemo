package com.example.utils

import java.util.*

/**
 * Day：2020/12/8 11:12 AM
 * @author zhanglei
 */
object PathUtils {
    /**
     * 转成小写
     */
    fun toLowerCase(s: String): String {
        return if (s.isEmpty()) s else s.toLowerCase(Locale.ROOT)
    }

    /**
     * 转成非null的字符串，如果为null返回空串
     */
    fun toNonNullString(s: String?): String? {
        return s ?: ""
    }
}