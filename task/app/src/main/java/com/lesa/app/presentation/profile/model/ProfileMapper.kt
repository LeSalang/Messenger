package com.lesa.app.presentation.profile.model

import com.lesa.app.domain.model.User

class ProfileMapper() {
    fun map(user: User) : ProfileUi {
        return ProfileUi(
            id = user.id,
            name = user.name,
            email = user.email,
            avatar = user.avatar,
            presence = user.presence.toUi()
        )
    }

    private fun User.Presence.toUi() : ProfileUi.Presence {
        return when (this) {
            User.Presence.ACTIVE -> ProfileUi.Presence.ACTIVE
            User.Presence.IDLE -> ProfileUi.Presence.IDLE
            User.Presence.OFFLINE -> ProfileUi.Presence.OFFLINE
        }
    }
}