package com.lesa.app.model_factories

import com.lesa.app.data.network.models.UserApiDto

object UserApiDtoFactory {
    fun create(
        id: Int = 10,
        deliveryEmail: String = "deliveryEmail",
        email: String = "email",
        name: String = "name",
        avatar: String = "avatar"
    ): UserApiDto {
        return UserApiDto(
            id = id,
            deliveryEmail = deliveryEmail,
            email = email,
            name = name,
            avatar = avatar
        )
    }
}