package com.example.plugins

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry


import com.example.plugin.AsmConfig
import com.example.plugin.TransFormClassVisitor

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES

/**
 * custom transform: tranform classes before dex
 * inputType、scope、isIncremental主要根据 task:transformClassesWithDex来设定
 */
class MixTransform extends Transform {

    private Project project

    public MixTransform(Project project) {
        this.project = project
    }

    private AsmConfig initConfig() {
        def configuration = project.traceMan
        AsmConfig config = new AsmConfig()
        config.MPluginConfigFile = configuration.traceConfigFile
        return config
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        def traceManConfig = project.traceMan
        String output = traceManConfig.output
        if (output == null || output.isEmpty()) {
            traceManConfig.output = project.getBuildDir().getAbsolutePath() + File.separator + "transform_output"
        }

        if (traceManConfig.open) {
            //读取配置
            AsmConfig traceConfig = initConfig()
            traceConfig.setPluginConfigFile()

            Collection<TransformInput> inputs = transformInvocation.inputs
            TransformOutputProvider outputProvider = transformInvocation.outputProvider
            if (outputProvider != null) {
                outputProvider.deleteAll()
            }

            //遍历 分为class文件变量和jar包的遍历
            inputs.each { TransformInput input ->
                input.directoryInputs.each { DirectoryInput directoryInput ->
                    traceSrcFiles(directoryInput, outputProvider, traceConfig)
                }

                input.jarInputs.each { JarInput jarInput ->
                    traceJarFiles(jarInput, outputProvider, traceConfig)
                }
            }
        }
    }

    @Override
    String getName() {
        return "traceManTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    /**
     * ASM的访问者模式进行插桩
     */
    static void traceSrcFiles(DirectoryInput directoryInput, TransformOutputProvider outputProvider, AsmConfig traceConfig) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name

                //根据配置的插桩范围决定要对某个class文件进行处理  过滤R文件、config、Manifest 等
                if (traceConfig.isNeedTraceClass(name)) {
                    //利用ASM的api对class文件进行访问
                    ClassReader classReader = new ClassReader(file.bytes)
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor cv = new TransFormClassVisitor(Opcodes.ASM5, classWriter, traceConfig)
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fos = new FileOutputStream(
                            file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }

        //处理完输出给下一任务作为输入
        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)
    }


    static void traceJarFiles(JarInput jarInput, TransformOutputProvider outputProvider, AsmConfig traceConfig) {
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
            //重命名输出文件,因为可能同名,会覆盖
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()

            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            if (tmpFile.exists()) {
                tmpFile.delete()
            }

            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))

            //循环jar包里的文件
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                if (traceConfig.isNeedTraceClass(entryName)) {
                    jarOutputStream.putNextEntry(zipEntry)
                    //读取字节码与分析
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    //用于拼接字节码
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    //定义在读取class字节码时会触发的事件，比如类头解析完成、注册解析、字段解析、方法解析等
                    ClassVisitor cv = new TransFormClassVisitor(Opcodes.ASM6, classWriter, traceConfig)
                    //给vistor 传递 jvm类构文件结构
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }

            jarOutputStream.close()
            jarFile.close()

            //处理完输出给下一任务作为输入
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)

            tmpFile.delete()
        }
    }
}