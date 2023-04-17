package com.example.minedemo

data class UserData(
    var name: String? = null,
    var age: Int? = null,
    var sex: Int? = null,
    var type:String?=null
) {
    override fun equals(other: Any?): Boolean {

        return when (other) {
            null -> false
            !is UserData -> false
            else -> (name == other.name) && (age == other.age) && (sex == other.sex)
        }
    }
}