package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.Logger
import com.example.mine.lib_gson_adapter.base.ClassKind
import kotlinx.metadata.Flag
import javax.lang.model.element.TypeElement

/**
 * Day：2023/4/27 16:36
 * @author zhanglei
 */
class KmClassKindResolver(
    private val belongingClass: TypeElement,
    private val kmClassCacheHolder: KmClassConvert,
    private val logger: Logger
) {
    private val kmClass by lazy {
        kmClassCacheHolder.get(belongingClass)
    }

    fun getClassKind(): ClassKind {
        return when {
            Flag.Class.IS_CLASS(kmClass.flags) -> ClassKind.CLASS
            Flag.Class.IS_ANNOTATION_CLASS(kmClass.flags) -> ClassKind.ANNOTATION
            Flag.Class.IS_ENUM_ENTRY(kmClass.flags) -> ClassKind.ENUM_ENTRY
            Flag.Class.IS_ENUM_CLASS(kmClass.flags) -> ClassKind.ENUM_CLASS
            Flag.Class.IS_INTERFACE(kmClass.flags) -> ClassKind.INTERFACE
            Flag.Class.IS_OBJECT(kmClass.flags) || Flag.Class.IS_COMPANION_OBJECT(kmClass.flags) -> ClassKind.OBJECT
            else -> {
                logger.e("unexpected class kind on class $belongingClass", belongingClass)
                throw IllegalArgumentException("unexpected class kind on class $belongingClass")
            }
        }
    }
}