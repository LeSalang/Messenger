package com.lesa.app.data.repositories

import com.lesa.app.data.network.Api
import com.lesa.app.data.network.models.toMessage
import com.lesa.app.domain.model.Message
import javax.inject.Inject

interface MessagesRepository {
    suspend fun getAllMessagesInStream(
        streamName: String,
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
    private val api: Api
) : MessagesRepository {

    override suspend fun getAllMessagesInStream(
        streamName: String,
        topicName: String
    ): List<Message> {
        val id = api.getOwnUser().id // TODO: request once after login
        return api.getAllMessagesInStream(
            narrow = "[{\"operator\": \"stream\", \"operand\": \"$streamName\"}]"
        ).messages.map {
            it.toMessage(id)
        }.filter {
            it.topic == topicName
        }
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
}