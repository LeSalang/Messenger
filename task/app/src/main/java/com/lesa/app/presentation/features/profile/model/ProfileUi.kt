package com.lesa.app.presentation.features.profile.model

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.lesa.app.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileUi(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String?,
    val presence: Presence,
) : Parcelable {

    @Parcelize
    enum class Presence(
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
}