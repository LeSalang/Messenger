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

    @Query("DELETE FROM messages WHERE topic_name = :topicName")
    suspend fun deleteAllInTopic(topicName: String)

    @Query("SELECT * FROM messages WHERE topic_name = :topicName AND stream_name = :streamName")
    suspend fun getMessagesInTopic(topicName: String, streamName: String): List<MessageEntity>
}