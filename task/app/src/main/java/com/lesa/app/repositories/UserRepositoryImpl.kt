package com.lesa.app.repositories

import com.lesa.app.api.Api
import com.lesa.app.model.User
import com.lesa.app.model.api_models.toUser

interface UserRepository {
    suspend fun getAllUsers(): List<User>
}

class UserRepositoryImpl(
    private val api: Api
) : UserRepository {
    var currentUserId: Int? = 1234

    override suspend fun getAllUsers(): List<User> {
        return api.getAllUsers().users.map {
            it.toUser()
        }
    }
}