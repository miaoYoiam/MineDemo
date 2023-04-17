package com.example.minedemo.data

import android.os.Parcel
import android.os.Parcelable

data class TextList(
    val user: TextUser? = null,
    val text: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(TextUser::class.java.classLoader),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(user, flags)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TextList> {
        override fun createFromParcel(parcel: Parcel): TextList {
            return TextList(parcel)
        }

        override fun newArray(size: Int): Array<TextList?> {
            return arrayOfNulls(size)
        }
    }

}

data class TextUser(val label: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TextUser> {
        override fun createFromParcel(parcel: Parcel): TextUser {
            return TextUser(parcel)
        }

        override fun newArray(size: Int): Array<TextUser?> {
            return arrayOfNulls(size)
        }
    }
}
