package com.lesa.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val name: String,
    val color: String,
    val streamName: String,
    val streamId: Int
) : Parcelable
