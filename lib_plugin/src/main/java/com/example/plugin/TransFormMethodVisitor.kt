package com.example.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * Create by cxzheng on 2019/6/4
 * Method Visitor
 */
class TransFormMethodVisitor(
    api: Int,
    mv: MethodVisitor?,
    access: Int,
    name: String?,
    desc: String?,
    className: String?,
    traceTestConfig: AsmConfig
) : AdviceAdapter(api, mv, access, name, desc) {
    companion object {
        private const val MAX_METHOD_NAME_LEGTH = 127
    }

    private var methodName: String? = null
    private var simpleMethodName: String? = null
    private var name: String? = null
    private var className: String? = null

    private var delegateOwnerClass: String? = null

    init {
        val methodSession = MethodSession.create(0, access, className, name, desc)
        this.methodName = methodSession.getMethodName()
        this.simpleMethodName = methodSession.getSimpleMethodName()
        this.className = className
        this.name = name
        this.delegateOwnerClass = traceTestConfig.mRootDelegateClass
    }

    override fun onMethodEnter() {
        super.onMethodEnter()
        val methodName = generatorMethodName()
        //入栈
        mv.visitLdcInsn(methodName)
        mv.visitMethodInsn(
            INVOKESTATIC,
            delegateOwnerClass,
            "startTrace",
            "(Ljava/lang/String;)V",
            false
        )

        mv.visitLdcInsn(methodName)
        mv.visitMethodInsn(
            INVOKESTATIC,
            delegateOwnerClass,
            "logger",
            "(Ljava/lang/String;)V",
            false
        )

        if (AsmConfig.NEED_LOG_TRANSFORM_INFO) {
            println("-----------method: ${simpleMethodName ?: "unkown"}")
        }
    }

    override fun onMethodExit(opcode: Int) {
        mv.visitLdcInsn(generatorMethodName())
        mv.visitMethodInsn(
            INVOKESTATIC,
            delegateOwnerClass,
            "endTrace",
            "(Ljava/lang/String;)V",
            false
        )
    }

    private fun generatorMethodName(): String? {
        var sectionName = methodName
        var length = sectionName?.length ?: 0
        if (length > MAX_METHOD_NAME_LEGTH && !sectionName.isNullOrBlank()) {
            val paramsIndex = sectionName.indexOf('(')
            if (paramsIndex > 0) {
                sectionName = sectionName.substring(0, paramsIndex)
            }
            length = sectionName.length
            if (length > MAX_METHOD_NAME_LEGTH) {
                sectionName = sectionName.substring(length - MAX_METHOD_NAME_LEGTH)
            }
        }
        return sectionName
    }

}