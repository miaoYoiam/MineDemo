package com.example.mine.lib_gson_adapter

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

/**
 * Day：2023/4/27 12:23
 * @author zhanglei
 */
class Logger(private val processingEnvironment: ProcessingEnvironment) {

    companion object {
        private const val TAG = "[PokeBean]"
    }

    fun d(message: String, target: Any? = null) {
        processingEnvironment.messager.printMessage(
            Diagnostic.Kind.OTHER,
            "$TAG $message",
            target?.tryCastElement()
        )
    }

    fun i(message: String, target: Any? = null) {
        processingEnvironment.messager.printMessage(
            Diagnostic.Kind.NOTE,
            "$TAG $message",
            target?.tryCastElement()
        )
    }

    fun w(message: String, target: Any? = null) {
        processingEnvironment.messager.printMessage(
            Diagnostic.Kind.WARNING,
            "$TAG $message",
            target?.tryCastElement()
        )
    }

    fun e(message: String, target: Any? = null) {
        processingEnvironment.messager.printMessage(
            Diagnostic.Kind.ERROR,
            "$TAG $message",
            target?.tryCastElement()
        )
    }

    private fun Any.tryCastElement(): Element? {
        return when (this) {
//            is IElementOwner -> this.target
            is Element -> this
            else -> null
        }
    }
}