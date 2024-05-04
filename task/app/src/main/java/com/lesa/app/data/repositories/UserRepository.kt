package com.lesa.app.data.repositories

import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.local.entities.toUser
import com.lesa.app.data.local.entities.toUserEntity
import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.AllPresenceApiDto
import com.lesa.app.data.network.models.toPresence
import com.lesa.app.data.network.models.toUser
import com.lesa.app.domain.model.User
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.Date
import javax.inject.Inject

interface UserRepository {
    suspend fun getUpdatedUsers() : List<User>
    suspend fun getCachedUsers() : List<User>
    suspend fun getOwnUser() : User
}

class UserRepositoryImpl @Inject constructor(
    private val api: Api,
    private val dao: UserDao
) : UserRepository {
    override suspend fun getUpdatedUsers(): List<User> {
        val list = coroutineScope {
            val allPresence = async {
                getAllPresence()
            }
            val allUsers = async {
                api.getAllUsers().users
            }
            val presencesMap = allPresence.await().presences
            return@coroutineScope allUsers.await().map {
                it.toUser(
                    presence = presencesMap[it.email].toPresence()
                )
            }
        }
        updateCachedUsers(list)
        val sortedList = list.sortedBy {
            it.name
        }
        return sortedList
    }

    override suspend fun getCachedUsers(): List<User> {
        val list = coroutineScope {
            val allUsers = dao.getAll()
            return@coroutineScope allUsers.map {
                it.toUser()
            }
        }
        val sortedList = list.sortedBy {
            it.name
        }
        return sortedList
    }

    override suspend fun getOwnUser(): User {
        val user = api.getOwnUser()
        return user.toUser(
            presence = User.Presence(
                status = User.Presence.Status.ACTIVE,
                timestamp = Date()
            )
        )
    }

    private suspend fun updateCachedUsers(users: List<User>) {
        val list = users.map {
            it.toUserEntity()
        }
        dao.updateUsers(list)
    }

    private suspend fun getAllPresence() : AllPresenceApiDto {
        return api.getAllUsersPresence()
    }
}