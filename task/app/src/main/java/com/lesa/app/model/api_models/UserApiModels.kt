package com.lesa.app.model.api_models

import com.lesa.app.model.User
import com.lesa.app.model.UserNetStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersApiDto(
    @SerialName("members")
    val users: List<UserApiDto>
)

@Serializable
data class UserApiDto(
    @SerialName("user_id")
    val id: Int,
    @SerialName("delivery_email")
    val deliveryEmail: String?,
    @SerialName("email")
    val email: String,
    @SerialName("full_name")
    val name: String,
    @SerialName("avatar_url")
    val avatar: String?

    //TODO
    /*@SerialName("user_id")
    val netStatus: UserNetStatus*/
)

fun UserApiDto.toUser() : User {
    return User(
        id = id,
        name = name,
        email = deliveryEmail ?: email,
        avatar = avatar,
        netStatus = UserNetStatus.ACTIVE
    )
}