package com.example.mine.lib_gson_adapter.base

/**
 * Day：2023/4/27 15:38
 * @author zhanglei
 *
 * @see com.google.gson.stream.JsonToken
 */
enum class JsonTokenDelegate(val jsonToken: String, val nextFuncExp: String) {
    INT("NUMBER", "nextInt()"),
    LONG("NUMBER", "nextLong()"),
    FLOAT("NUMBER", "nextDouble().toFloat()"),
    DOUBLE("NUMBER", "nextDouble()"),
    STRING("STRING", "nextString()"),
    BOOLEAN("BOOLEAN", "nextBoolean()"),

    LIST("BEGIN_ARRAY", ""),

    JAVA_LIST("BEGIN_ARRAY", ""),

    SET("BEGIN_ARRAY", ""),

    JAVA_SET("BEGIN_ARRAY", ""),

    ENUM("STRING", "nextString()"),
    MAP("BEGIN_OBJECT", ""),
    JAVA_MAP("BEGIN_OBJECT", ""),
    OBJECT("BEGIN_OBJECT", "");

    fun isPrimitive() = this in listOf(INT, LONG, FLOAT, DOUBLE, STRING, BOOLEAN)

    fun isObject() = this == OBJECT

    fun isArray() = this in listOf(LIST, SET, JAVA_LIST, JAVA_SET)

    fun isMap() = this in listOf(MAP, JAVA_MAP)

    fun isEnum() = this == ENUM
}