package com.example.data

import com.example.lib_annotation.PageType

/**
 * Day：2020/12/8 12:06 PM
 * @author zhanglei
 */
class PageNode(var id: String, val type: PageType, val allowMultiMode: Boolean) {
    var tag: String? = null

    override fun toString(): String {
        return "\nPageNode params is (id:$id  type:$type allowMultiMode:$allowMultiMode tag:$tag)"
    }
}