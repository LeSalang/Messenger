package com.lesa.app.model_factories

import com.lesa.app.data.local.entities.UserEntity

object UserEntityFactory {
    fun create(
        id: Int = 1,
        name: String = "userName1",
        email: String = "email1",
        avatar: String = "avatar1",
        lastActivity: Long = 1000000000,
        status: Int = 1
    ): UserEntity {
        return UserEntity(
            id = id,
            name = name,
            email = email,
            avatar = avatar,
            lastActivity = lastActivity,
            status = status
        )
    }
}