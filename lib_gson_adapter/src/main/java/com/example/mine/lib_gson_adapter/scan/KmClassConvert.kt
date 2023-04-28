package com.example.mine.lib_gson_adapter.scan

import com.example.mine.lib_gson_adapter.Logger
import kotlinx.metadata.KmClass
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.lang.model.element.TypeElement

/**
 * Day：2023/4/27 16:09
 * @author zhanglei
 */
class KmClassConvert(private val logger: Logger) {
    private val cache = mutableMapOf<String, KmClass>()

    fun get(typeElement: TypeElement): KmClass {
        val name = typeElement.qualifiedName.toString()
        return cache.getOrPut(name) {
            val kmClass = typeElement.asKmClass()
                ?: throw IllegalStateException("Unexpected metadata received for element $name")
            kmClass
        }
    }

    private fun TypeElement.asKmClass(): KmClass?{
        val metadataAnnotation = getAnnotation(Metadata::class.java) ?: run {
            logger.e("@Metadata annotation not found", this)
            return null
        }
        val header = KotlinClassHeader(
            kind = metadataAnnotation.kind,
            metadataVersion = metadataAnnotation.metadataVersion,
//            bytecodeVersion = metadataAnnotation.bytecodeVersion,
            data1 = metadataAnnotation.data1,
            data2 = metadataAnnotation.data2,
            extraString = metadataAnnotation.extraString,
            packageName = metadataAnnotation.packageName,
            extraInt = metadataAnnotation.extraInt
        )
        val kotlinClassMetadata = KotlinClassMetadata.read(header) ?: run {
            logger.e("parse KotlinClassMetadata return null", this)
            return null
        }
        kotlinClassMetadata as? KotlinClassMetadata.Class ?: run {
            logger.e("parse result is NOT KotlinClassMetadata.Class", this)
            return null
        }
        return kotlinClassMetadata.toKmClass()
    }
}