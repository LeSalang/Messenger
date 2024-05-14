package com.lesa.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lesa.app.domain.model.User
import java.util.Date

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("full_name") val name: String,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("avatar_url") val avatar: String?,
    @ColumnInfo("last_activity") val lastActivity: Long?,
    @ColumnInfo("status") val status: Int
)

fun UserEntity.toUser() : User {
    return User(
        id = id,
        name = name,
        email = email,
        avatar = avatar,
        presence = User.Presence(
            status = User.Presence.Status.entries.getOrNull(status) ?: User.Presence.Status.OFFLINE,
            timestamp = lastActivity?.let { Date(it) }
        )
    )
}