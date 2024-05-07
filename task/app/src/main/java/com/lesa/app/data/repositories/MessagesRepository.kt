package com.lesa.app.data.repositories

import com.lesa.app.data.local.dao.MessageDao
import com.lesa.app.data.local.entities.toMessage
import com.lesa.app.data.local.entities.toMessageEntity
import com.lesa.app.data.network.Api
import com.lesa.app.data.network.Api.Companion.NEWEST_MESSAGE_ANCHOR
import com.lesa.app.data.network.models.MessageFilter.Companion.createNarrow
import com.lesa.app.data.network.models.toMessage
import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.MessageAnchor
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

interface MessagesRepository {
    suspend fun getMessagesInTopic(
        streamName: String,
        topicName: String,
        messageAnchor: MessageAnchor
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

    override suspend fun getMessagesInTopic(
        streamName: String,
        topicName: String,
        messageAnchor: MessageAnchor
    ): List<Message> {
        val id = api.getOwnUser().id // TODO: request once after login
        val narrow = createNarrow(streamName, topicName)
        val anchor = when (messageAnchor) {
            is MessageAnchor.Message -> messageAnchor.id.toString()
            MessageAnchor.Newest -> NEWEST_MESSAGE_ANCHOR
        }
        val list = api.getAllMessagesInStream(
            narrow = narrow,
            anchor = anchor
        )
        .messages
        .map { it.toMessage(id) }
        updateCachedMessages(list, topicName)
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

    private suspend fun updateCachedMessages(
        messages: List<Message>,
        topicName: String
    ) {
        val newList = messages.map { it.toMessageEntity() }
        val oldList = dao.getAll()
        var finalList = (newList + oldList).sortedBy { it.id }
        if (finalList.size > 50) {
            finalList = finalList.drop(finalList.size - 50)
            dao.deleteAllInTopic(topicName)
        }
        dao.updateMessages(finalList)
    }
}