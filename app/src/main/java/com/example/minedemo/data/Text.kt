package com.example.minedemo.data

import android.os.Parcel
import android.os.Parcelable

data class Text(
    val text: String? = null
) : Parcelable {
//    var textList:TextList?=null

    var textList: List<TextList>? = mutableListOf()

    constructor(parcel: Parcel) : this(parcel.readString()) {
        textList = parcel.createTypedArrayList(TextList)
        //        textList = parcel.readParcelable(TextList::class.java.classLoader)

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeTypedList(textList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Text> {
        override fun createFromParcel(parcel: Parcel): Text {
            return Text(parcel)
        }

        override fun newArray(size: Int): Array<Text?> {
            return arrayOfNulls(size)
        }
    }


}