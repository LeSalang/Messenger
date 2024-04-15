package com.lesa.app.repositories

import com.lesa.app.api.Api
import com.lesa.app.model.Message
import com.lesa.app.model.api_models.toMessage

interface MessagesRepository {
    suspend fun getAllMessagesInStream(
        streamName: String,
        topicName: String
    ) : List<Message>
}

class MessagesRepositoryImpl(
    private val api: Api
) : MessagesRepository {
    override suspend fun getAllMessagesInStream(
        streamName: String,
        topicName: String
    ): List<Message> {
        val id = api.getOwnUser().id
        return api.getAllMessagesInStream(
            narrow = "[{\"operator\": \"stream\", \"operand\": \"$streamName\"}]"
        ).messages.map {
            it.toMessage(id)
        }.filter {
            it.topic == topicName
        }
    }
}