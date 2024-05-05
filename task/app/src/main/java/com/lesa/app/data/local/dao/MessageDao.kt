package com.lesa.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lesa.app.data.local.entities.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM messages")
    suspend fun getAll(): List<MessageEntity>
}