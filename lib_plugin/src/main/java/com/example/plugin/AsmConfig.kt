package com.example.plugin

import java.util.HashSet

/**
 * Create by cxzheng on 2019/6/4
 */
class AsmConfig {
    companion object {
        //一些默认无需插桩的类
        private val UNNEED_TRANSFROM_CLASS = arrayOf("R.class", "R$", "Manifest", "BuildConfig")
        //是否需要打印出所有被插桩的类和方法
        const val NEED_LOG_TRANSFORM_INFO = true
    }

    //插桩配置文件
    var mPluginConfigFile: String? = null

    //需插桩的包
    private val mNeedTracePackageMap: HashSet<String> by lazy {
        HashSet<String>()
    }

    //在需插桩的包范围内的 无需插桩的白名单
    private val mWhiteClassMap: HashSet<String> by lazy {
        HashSet<String>()
    }

    //在需插桩的包范围内的 无需插桩的包名
    private val mWhitePackageMap: HashSet<String> by lazy {
        HashSet<String>()
    }

    //插桩代码所在类
    var mRootDelegateClass: String? = null

    fun isNeedTraceClass(fileName: String): Boolean {
        var isNeed = true
        if (fileName.endsWith(".class")) {
            for (unTraceCls in UNNEED_TRANSFROM_CLASS) {
                if (fileName.contains(unTraceCls)) {
                    isNeed = false
                    break
                }
            }
        } else {
            isNeed = false
        }
        return isNeed
    }

    private fun isInNeedTracePackage(className: String): Boolean {
        var isIn = false
        mNeedTracePackageMap.forEach {
            if (className.contains(it)) {
                isIn = true
                return@forEach
            }

        }
        return isIn
    }

    private fun isInWhitePackage(className: String): Boolean {
        var isIn = false
        mWhitePackageMap.forEach {
            if (className.contains(it)) {
                isIn = true
                return@forEach
            }

        }
        return isIn
    }

    private fun isInWhiteClass(className: String): Boolean {
        var isIn = false
        mWhiteClassMap.forEach {
            if (className == it) {
                isIn = true
                return@forEach
            }

        }
        return isIn
    }

    //判断是否是traceConfig.txt中配置范围的类
    fun isConfigTraceClass(className: String): Boolean {
        return if (mNeedTracePackageMap.isEmpty()) {
            //全量插桩
            !(isInWhitePackage(className) || isInWhiteClass(className))
        } else {
            //按需插桩
            if (isInNeedTracePackage(className)) {
                !(isInWhitePackage(className) || isInWhiteClass(className))
            } else {
                false
            }
        }
    }


    /**
     * 插桩配置
     */
    fun setPluginConfigFile() {
        println("plugin config")

        mNeedTracePackageMap.add("com/example/minedemo")
//        mWhiteClassMap.add()
//        mWhitePackageMap.add()
        mRootDelegateClass = "com/example/minedemo/asm/AsmRootDelegate"
    }

}


