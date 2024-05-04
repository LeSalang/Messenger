package com.lesa.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lesa.app.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Transaction
    suspend fun updateUsers(users: List<UserEntity>) {
        deleteAllUsers()
        insertAllUsers(users)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<UserEntity>)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>
}