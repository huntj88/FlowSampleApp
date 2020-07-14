package me.jameshunt.inmotiontestapplication.colors

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Color(
    val red: Int,
    val green: Int,
    val blue: Int
) : Parcelable
