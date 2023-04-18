package com.example.plugins

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Day：2023/4/14 17:23
 * @author zhanglei
 */
class MixPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println '-----------> MethodTraceMan Plugin apply start <----------- '

        //创建 traceMan
        project.extensions.create("traceMan", MixConfig)

        //注册
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new MixTransform(project))

    }
}