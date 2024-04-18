package com.lesa.app.data.repositories

import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.AllPresenceApiDto
import com.lesa.app.data.network.models.toPresence
import com.lesa.app.data.network.models.toUser
import com.lesa.app.domain.model.User
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface UserRepository {
    suspend fun getAllUsers() : List<User>
    suspend fun getOwnUser() : User
}

class UserRepositoryImpl(
    private val api: Api
) : UserRepository {
    override suspend fun getAllUsers(): List<User> {
        val list =  coroutineScope {
            val allPresence = async {
                getAllPresence()
            }
            val allUsers = async {
                api.getAllUsers().users
            }
            val presencesMap = allPresence.await().presences
            return@coroutineScope allUsers.await().map {
                it.toUser(
                    presence = presencesMap[it.email]?.toPresence() ?: User.Presence.OFFLINE
                )
            }
        }
        return list
    }

    override suspend fun getOwnUser(): User {
        val user = api.getOwnUser()
        return user.toUser(presence = User.Presence.ACTIVE)
    }

    private suspend fun getAllPresence() : AllPresenceApiDto {
        return api.getAllUsersPresence()
    }
}