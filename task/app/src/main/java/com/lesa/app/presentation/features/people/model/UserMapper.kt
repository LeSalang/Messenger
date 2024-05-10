package com.lesa.app.presentation.features.people.model

import com.lesa.app.domain.model.User

object UserMapper {
    fun map(user: User) : UserUi {
        return UserUi(
            id = user.id,
            name = user.name,
            email = user.email,
            avatar = user.avatar,
            presence = user.presence.toUi()
        )
    }

    private fun User.Presence?.toUi() : UserUi.Presence {
        if (this == null) return UserUi.Presence.OFFLINE
        return when (this.status) {
            User.Presence.Status.ACTIVE -> UserUi.Presence.ACTIVE
            User.Presence.Status.IDLE -> UserUi.Presence.IDLE
            User.Presence.Status.OFFLINE -> UserUi.Presence.OFFLINE
        }
    }
}