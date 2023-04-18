package com.example.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Create by cxzheng on 2019/6/4
 * Class Visitor
 */
class TransFormClassVisitor(api: Int, cv: ClassVisitor?, var traceTestConfig: AsmConfig) : ClassVisitor(api, cv) {

    private var className: String? = null
    private var isABSClass = false
    private var isRootDelegateClass = false
    private var isConfigTraceClass = false


    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)

        this.className = name
        //抽象方法或者接口
        if (access and Opcodes.ACC_ABSTRACT > 0 || access and Opcodes.ACC_INTERFACE > 0) {
            this.isABSClass = true
        }

        //插桩代码所属类
        val resultClassName = name?.replace(".", "/")
        if (resultClassName == traceTestConfig.mRootDelegateClass) {
            this.isRootDelegateClass = true
        }

        //是否是配置的需要插桩的类
        name?.let { className ->
            isConfigTraceClass = traceTestConfig.isConfigTraceClass(className)
        }

        val isNotNeedTraceClass = isABSClass || isRootDelegateClass || !isConfigTraceClass
        if (AsmConfig.NEED_LOG_TRANSFORM_INFO && !isNotNeedTraceClass) {
            println("Transform-class: ${className ?: "未知"}  ")
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val isConstructor = MethodFilter.isConstructor(name)
        val validVisit = !isABSClass && !isRootDelegateClass && !isConfigTraceClass && !isConstructor
        return if (!validVisit) {
            super.visitMethod(access, name, desc, signature, exceptions)
        } else {
            val mv = cv.visitMethod(access, name, desc, signature, exceptions)
            TransFormMethodVisitor(api, mv, access, name, desc, className, traceTestConfig)
        }
    }

    override fun visitField(access: Int, name: String?, descriptor: String?, signature: String?, value: Any?): FieldVisitor {
        return super.visitField(access, name, descriptor, signature, value)
    }
}