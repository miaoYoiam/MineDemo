package com.example.mine.lib_gson_adapter.base

/**
 * Day：2023/4/27 14:25
 * @author zhanglei
 *
 * @param cKind: javax.lang.model.element.ElementKind
 */
abstract class ClassScanner {
    abstract val cKind: ClassKind

    abstract val classType: IType

    abstract val field: List<IFiled>
}