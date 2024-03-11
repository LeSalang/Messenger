package com.lesa.homework_1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val number: String
) : Parcelable
