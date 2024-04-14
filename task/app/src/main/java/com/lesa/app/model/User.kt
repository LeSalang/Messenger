package com.lesa.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String?,
    val netStatus: UserNetStatus
) : Parcelable
