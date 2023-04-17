package com.example.lib_compiler

import com.example.lib_annotation.PagePath
import com.example.lib_annotation.PageType
import javax.lang.model.element.TypeElement

/**
 * Day：2020/12/3 5:32 PM
 * @author zhanglei
 */
class PagePathProxy(classElement: TypeElement) {
    var annotationElement = classElement
    var id: String? = null
    var type: PageType? = null
    var allowMultiMode: Boolean = false

    init {
        val annotation = classElement.getAnnotation(PagePath::class.java)
        id = annotation.id
        type = annotation.type
        allowMultiMode = annotation.allowMultiMode
    }

    fun getTypeElement(): TypeElement = annotationElement

}