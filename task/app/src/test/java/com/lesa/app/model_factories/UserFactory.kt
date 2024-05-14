package com.lesa.app.model_factories

import com.lesa.app.domain.model.User
import java.util.Date

object UserFactory {
    fun create(
        id: Int = 1,
        name: String = "name",
        email: String = "email",
        avatar: String = "avatar",
        status: User.Presence.Status = User.Presence.Status.ACTIVE,
        timestamp: Date = Date(1_000_000_000)
    ): User {
        return User(
            id = id,
            name = name,
            email = email,
            avatar = avatar,
            presence = User.Presence(
                status = status,
                timestamp = timestamp
            )
        )
    }
}