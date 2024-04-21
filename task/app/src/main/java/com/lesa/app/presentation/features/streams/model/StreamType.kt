package com.lesa.app.presentation.features.streams.model

import android.os.Parcelable
import com.lesa.app.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class StreamType(val title: Int) : Parcelable {
    SUBSCRIBED(title = R.string.channels_subscribed),
    ALL(title = R.string.channels_all_streams)
}