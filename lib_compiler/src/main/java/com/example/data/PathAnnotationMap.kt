package com.example.data

import com.example.utils.PathUtils
import java.util.*

/**
 * Day：2020/12/8 11:18 AM
 * @author zhanglei
 */
class PathAnnotationMap<T> {
    private val mMap = HashMap<String, T>()

    fun put(key: String?, value: T?): T? {
        return if (key.isNullOrEmpty() || value == null) {
            null
        } else mMap.put(PathUtils.toLowerCase(key), value)
    }

    operator fun get(key: String?): T? {
        if (key.isNullOrEmpty()) {
            return null
        }
        return mMap[PathUtils.toLowerCase(key)]
    }

    fun remove(key: String?): T? {
        if (key.isNullOrEmpty()) {
            return null
        }
        return mMap.remove(PathUtils.toLowerCase(key))
    }

    fun containsKey(key: String?): Boolean {
        if (key.isNullOrEmpty()) {
            return false
        }
        return mMap.containsKey(PathUtils.toLowerCase(key))
    }
}