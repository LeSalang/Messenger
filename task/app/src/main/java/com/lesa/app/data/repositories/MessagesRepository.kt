package com.lesa.app.data.repositories

import com.lesa.app.data.local.dao.MessageDao
import com.lesa.app.data.local.entities.toMessage
import com.lesa.app.data.local.entities.toMessageEntity
import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.MessageFilter.Companion.createNarrow
import com.lesa.app.data.network.models.toMessage
import com.lesa.app.domain.model.Message
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface MessagesRepository {
    suspend fun getAllMessagesInTopic(
        streamName: String,
        topicName: String
    ) : List<Message>

    suspend fun getAllCachedMessagesInTopic(
        topicName: String
    ) : List<Message>

    suspend fun sendMessage(
        content: String,
        topicName: String,
        streamId: Int
    ) : Int

    suspend fun addReaction(
        messageId: Int,
        emojiName: String
    )

    suspend fun deleteReaction(
        messageId: Int,
        emojiName: String
    )

    suspend fun getMessage(
        messageId: Int
    ) : Message
}

class MessagesRepositoryImpl @Inject constructor(
    private val api: Api,
    private val dao: MessageDao
) : MessagesRepository {

    override suspend fun getAllMessagesInTopic(
        streamName: String,
        topicName: String
    ): List<Message> {
        val id = api.getOwnUser().id // TODO: request once after login
        val narrow = createNarrow(streamName, topicName)
        val list = api.getAllMessagesInStream(
                narrow = narrow
            ).messages
            .map {
                it.toMessage(id)
            }
        updateCachedUsers(list)
        return list
    }

    override suspend fun getAllCachedMessagesInTopic(topicName: String): List<Message> {
        val list = coroutineScope {
            val allMessages = dao.getAll()
            return@coroutineScope allMessages.map {
                it.toMessage()
            }.filter {
                it.topic == topicName
            }
        }
        return list
    }

    override suspend fun getMessage(messageId: Int) : Message {
        val id = api.getOwnUser().id // TODO: request once after login
        return api.getMessage(messageId = messageId).message.toMessage(id)
    }

    override suspend fun sendMessage(
        content: String,
        topicName: String,
        streamId: Int
    ) : Int {
        return api.sendMessage(
            streamId = streamId,
            topicName = topicName,
            content = content
        ).id
    }

    override suspend fun addReaction(messageId: Int, emojiName: String) {
        api.addReaction(
            messageId = messageId,
            emojiName = emojiName
        )
    }

    override suspend fun deleteReaction(messageId: Int, emojiName: String) {
        api.deleteReaction(
            messageId = messageId,
            emojiName = emojiName
        )
    }

    private suspend fun updateCachedUsers(messages: List<Message>) {
        val list = messages.map {
            it.toMessageEntity()
        }
        dao.updateMessages(list)
    }
}