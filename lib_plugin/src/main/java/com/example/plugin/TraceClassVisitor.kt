package com.example.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Create by cxzheng on 2019/6/4
 * Class Visitor
 */
class TraceClassVisitor(api: Int, cv: ClassVisitor?, var traceTestConfig: TestConfig) : ClassVisitor(api, cv) {

    private var className: String? = null
    private var isABSClass = false
    private var isBeatClass = false
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
        if (resultClassName == traceTestConfig.mBeatClass) {
            this.isBeatClass = true
        }

        //是否是配置的需要插桩的类
        name?.let { className ->
            isConfigTraceClass = traceTestConfig.isConfigTraceClass(className)
        }

        val isNotNeedTraceClass = isABSClass || isBeatClass || !isConfigTraceClass
        if (traceTestConfig.mIsNeedLogTraceInfo && !isNotNeedTraceClass) {
            println("MethodTraceMan-trace-class: ${className ?: "未知"}")
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
        //抽象方法、构造方法、不是插桩范围内的方法，则不进行插桩
        return if (isABSClass || isBeatClass || !isConfigTraceClass || isConstructor) {
            super.visitMethod(access, name, desc, signature, exceptions)
        } else {
            val mv = cv.visitMethod(access, name, desc, signature, exceptions)
            TraceMethodVisitor(api, mv, access, name, desc, className, traceTestConfig)
        }
    }

    override fun visitField(access: Int, name: String?, descriptor: String?, signature: String?, value: Any?): FieldVisitor {
        return super.visitField(access, name, descriptor, signature, value)
    }
}