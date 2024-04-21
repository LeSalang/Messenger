package com.lesa.app.presentation.features.chat.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicUi(
    val name: String,
    val color: String,
    val streamName: String,
    val streamId: Int
) : Parcelable