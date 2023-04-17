package com.example.utils

import com.example.lib_compiler.PagePathProcessor
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.*
import javax.lang.model.util.ElementFilter

/**
 * Day：2020/12/1 6:54 PM
 * @author zhanglei
 */
object ProcessorUtils {

    fun getElementsFor(clazz: Class<out Annotation?>?, env: RoundEnvironment): List<TypeElement?>? {
        val annotatedElements: Collection<Element?> = env.getElementsAnnotatedWith(clazz)
        return ElementFilter.typesIn(annotatedElements)
    }

    fun writeIndexer(indexer: TypeSpec, processingEnv: ProcessingEnvironment) {
        writeClass(
            COMPILER_PACKAGE_NAME,
            indexer,
            processingEnv
        )
    }

    fun writeClass(packageName: String?, clazz: TypeSpec, processingEnv: ProcessingEnvironment) {
        try {
            JavaFile.builder(packageName, clazz)
                .skipJavaLangImports(true)
                .build()
                .writeTo(processingEnv.filer)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    var COMPILER_PACKAGE_NAME: String = PagePathProcessor::class.java.getPackage().name


}