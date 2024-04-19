package com.lesa.app.presentation.features.user.model

import com.lesa.app.domain.model.User

class UserMapper() {
    fun map(user: User) : UserUi {
        return UserUi(
            name = user.name,
            avatar = user.avatar,
            presence = user.presence.toUi()
        )
    }

    private fun User.Presence.toUi() : UserUi.Presence {
        return when (this) {
            User.Presence.ACTIVE -> UserUi.Presence.ACTIVE
            User.Presence.IDLE -> UserUi.Presence.IDLE
            User.Presence.OFFLINE -> UserUi.Presence.OFFLINE
        }
    }
}