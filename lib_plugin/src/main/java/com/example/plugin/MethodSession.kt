package com.example.plugin

import org.objectweb.asm.Opcodes

/**
 * Create by cxzheng on 2019/6/4
 */
class MethodSession {

    private var id: Int = 0
    private var accessFlag: Int = 0
    private var className: String? = null
    private var methodName: String? = null
    private var desc: String? = null

    companion object {
        fun create(id: Int, accessFlag: Int, className: String?, methodName: String?, desc: String?): MethodSession {
            val methodSession = MethodSession()
            methodSession.id = id
            methodSession.accessFlag = accessFlag
            methodSession.className = className?.replace("/", ".")
            methodSession.methodName = methodName
            methodSession.desc = desc?.replace("/", ".")
//            println("id:$id  accessFlag:$accessFlag className:$className  methodName:$methodName  desc:$desc")
            return methodSession
        }
    }


    fun getMethodName(): String {
//        return if (desc == null || isNativeMethod()) {
//            this.className + "." + this.methodName
//        } else {
//            this.className + "." + this.methodName + "." + desc
//        }
        return this.className + "." + this.methodName
    }

    fun getSimpleMethodName() = this.methodName


    override fun toString(): String {
        return if (desc == null || isNativeMethod()) {
            "$id,$accessFlag,$className $methodName"
        } else {
            "$id,$accessFlag,$className $methodName $desc"
        }
    }


    private fun isNativeMethod(): Boolean {
        return accessFlag and Opcodes.ACC_NATIVE != 0
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is MethodSession) {
            val tm = obj as MethodSession?
            return tm!!.getMethodName() == getMethodName()
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}