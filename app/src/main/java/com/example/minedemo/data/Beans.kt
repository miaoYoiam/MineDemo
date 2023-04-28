package com.example.minedemo.data

import com.example.lib_annotation.PokeBean
import com.google.gson.annotations.SerializedName

@PokeBean
data class Foo(
//    override val parentIntValue: Int = 0,
//    override val parentStringValue: String = "",
//    override val parentLongValue: Long = 0L,

    @SerializedName("foo_boolean")
    val booleanValue: Boolean = false,
    @SerializedName("foo_double")
    val doubleValue: Double = 0.0,
    @SerializedName("foo_bar")
    val bar: Bar = Bar(),
    @SerializedName("foo_list_long")
    val listLong: List<Long> = listOf(),
    @SerializedName("foo_list_bar")
    val listBar: List<Bar> = listOf(),
    @SerializedName("foo_set_double")
    val setLong: Set<Double> = setOf(),
    @SerializedName("foo_set_bar")
    val setBar: Set<Bar> = setOf(),
    @SerializedName("foo_list_list_long")
    val nestedList: List<List<Long>> = listOf(),
    @SerializedName("foo_list_set_long")
    val listSet: List<Set<Long>> = listOf(),
    @SerializedName("foo_nullable_bean")
    val nullableBean: NullableBean = NullableBean(),
    @SerializedName("foo_test_enum")
    val testEnum: TestEnum = TestEnum.HELLO,
    @SerializedName("var_bean")
    val varFieldBean: VarFieldBean = VarFieldBean()
) : FooParent() {

    //todo body super里面的参数 val 不应该编译
    var bodyValIntValue: Int = 0

    var bodyVarIntValue: Int = 0
}

open class FooParent(
    open var parentIntValue: Int = 0,
    @SerializedName("foo_string")
    open var parentStringValue: String = "",
    @SerializedName("foo_long")
    open var parentLongValue: Long = 0L
) {
    var parentBodyValIntValue: Int = 0

    var parentBodyVarIntValue: Int = 0
}

@PokeBean
data class Bar(
    @SerializedName("bar_int")
    val intValue: Int = 0,
    @SerializedName("bar_long")
    val longValue: Long = 0L,
    @SerializedName("bar_string")
    val stringValue: String = ""
)

@PokeBean
data class NullableBean(
    @SerializedName("nullable_int")
    val intValue: Int? = null,
    @SerializedName("nullable_string")
    val stringValue: String? = null,
    @SerializedName("nullable_long")
    val longValue: Long? = null,
    @SerializedName("nullable_boolean")
    val booleanValue: Boolean? = null,
    @SerializedName("nullable_double")
    val doubleValue: Double? = null,
    @SerializedName("nullable_bar")
    val bar: Bar? = null,
    @SerializedName("nullable_list_long")
    val list: List<Long>? = null,
    @SerializedName("nullable_list_bar")
    val listBar: List<Bar>? = null
)

@PokeBean
data class VarFieldBean(
    @SerializedName("var_int")
    var intValue: Int = 0,
    @SerializedName("var_long")
    var longValue: Long = 0L,
    @SerializedName("val_double")
    val doubleValue: Double = 0.0
) {

    @SerializedName("var_out_constructor_list_long")
    var outConstructorListLongValue: List<Long> = listOf()

    var testVal: Int = 0
}


enum class TestEnum(val intValue: Int) {
    HELLO(1),
    HI(2),
    FINE(3),
    THANKS(4)
}