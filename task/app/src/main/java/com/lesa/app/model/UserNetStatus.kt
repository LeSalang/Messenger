package com.lesa.app.model

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.lesa.app.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class UserNetStatus(
    @StringRes val text: Int,
    @ColorRes val color: Int,
) : Parcelable {
    ACTIVE(
        R.string.status_active, R.color.green
    ),
    IDLE(
        R.string.status_idle, R.color.orange
    ),
    OFFLINE(
        R.string.status_offline, R.color.red
    )
}