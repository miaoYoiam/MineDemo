package com.example.mine.lib_gson_adapter.typeadapter

import com.example.mine.lib_gson_adapter.PEEKED
import com.example.mine.lib_gson_adapter.TEMP_FIELD_PREFIX
import com.example.mine.lib_gson_adapter.TYPE_ADAPTER
import com.example.mine.lib_gson_adapter.base.ElementType
import com.squareup.kotlinpoet.ClassName

fun String.firstCharLowerCase() =
    this.replaceFirst(this.first(), this.first().toLowerCase())

fun String.firstChatUpperCase() =
    this.replaceFirst(this.first(), this.first().toUpperCase())

fun ElementType.flatten(): String = buildString {
    append(ClassName.bestGuess(rawType).simpleName)
    generics.forEach { append(it.flatten()) }
}

fun ElementType.getTypeAdapterFieldName(): String {
    return "${flatten()}$TYPE_ADAPTER".firstCharLowerCase()
}

fun ElementType.getTypeAdapterClassName(): ClassName {
    return ClassName.bestGuess("$rawType$TYPE_ADAPTER")
}

fun ElementType.getReadingTempFieldName(): String {
    return "${TEMP_FIELD_PREFIX}Reading${flatten()}"
}

fun ElementType.getWritingTempFieldName(fieldName: String): String {
    return "${TEMP_FIELD_PREFIX}Writing${flatten()}${fieldName.firstChatUpperCase()}"
}

fun ElementType.getPeekedFieldName(): String {
    return "${PEEKED}${flatten()}"
}