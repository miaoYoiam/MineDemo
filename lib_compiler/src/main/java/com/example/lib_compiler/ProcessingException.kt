package com.example.lib_compiler

import javax.lang.model.element.Element

/**
 * Day：2020/12/3 5:41 PM
 * @author zhanglei
 */
class ProcessingException(element: Element, msg: String, vararg args: Any?) :
    Exception(String.format(msg, args)) {

    var element: Element? = null

    init {
        this.element = element
    }
}