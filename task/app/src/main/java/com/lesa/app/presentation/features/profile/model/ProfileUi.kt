package com.lesa.app.presentation.features.profile.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileUi(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String?
) : Parcelable