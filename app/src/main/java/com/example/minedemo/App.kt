package com.example.minedemo

import android.app.Application
import android.content.Context

/**
 * Day：4/26/21 3:20 PM
 * @author zhanglei
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        @JvmStatic
        lateinit var context: Application
    }
}