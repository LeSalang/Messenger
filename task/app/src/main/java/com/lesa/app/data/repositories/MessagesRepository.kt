package com.lesa.app.data.repositories

import android.net.Uri
import com.lesa.app.data.local.dao.MessageDao
import com.lesa.app.data.local.entities.toMessage
import com.lesa.app.data.local.entities.toMessageEntity
import com.lesa.app.data.network.Api
import com.lesa.app.data.network.Api.Companion.NEWEST_MESSAGE_ANCHOR
import com.lesa.app.data.network.models.MessageFilter.Companion.createNarrow
import com.lesa.app.data.network.models.toMessage
import com.lesa.app.data.utils.FileRequestBodyFactory
import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.MessageAnchor
import javax.inject.Inject

interface MessagesRepository {
    suspend fun getMessages(
        streamName: String,
        topicName: String?,
        messageAnchor: MessageAnchor
    ) : List<Message>

    suspend fun getAllCachedMessages(
        topicName: String?,
        streamName: String
    ) : List<Message>

    suspend fun sendMessage(
        content: String,
        topicName: String?,
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

    suspend fun uploadFile(
        name: String,
        uri: Uri
    ) : String

    suspend fun deleteMessage(
        messageId: Int
    )

    suspend fun editMessageContent(
        messageId: Int,
        content: String
    )

    suspend fun changeMessageTopic(
        messageId: Int,
        topicName: String
    )
}

class MessagesRepositoryImpl @Inject constructor(
    private val api: Api,
    private val dao: MessageDao,
    private val fileRequestBodyFactory: FileRequestBodyFactory
) : MessagesRepository {

    override suspend fun getMessages(
        streamName: String,
        topicName: String?,
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
        updateCachedMessages(list, topicName = topicName, streamName = streamName)
        return list
    }

    override suspend fun getAllCachedMessages(
        topicName: String?,
        streamName: String
    ): List<Message> {
        val allMessages = if (topicName == null) {
            dao.getMessagesInStream(streamName = streamName)
        } else {
            dao.getMessagesInTopic(topicName = topicName, streamName = streamName)
        }
        return allMessages.map {
            it.toMessage()
        }
    }

    override suspend fun getMessage(messageId: Int) : Message {
        val id = api.getOwnUser().id // TODO: request once after login
        return api.getMessage(messageId = messageId).message.toMessage(id)
    }

    override suspend fun uploadFile(name: String, uri: Uri) : String {
        val multipartBody = fileRequestBodyFactory.createRequestBody(uri = uri, name = name)
        return api.uploadFile(file = multipartBody).uri
    }

    override suspend fun deleteMessage(messageId: Int) {
        api.deleteMessage(messageId = messageId)
    }

    override suspend fun editMessageContent(messageId: Int, content: String) {
        api.editMessageContent(messageId = messageId, content = content)
    }

    override suspend fun changeMessageTopic(messageId: Int, topicName: String) {
        api.changeMessageTopic(messageId = messageId, topicName = topicName)
    }

    override suspend fun sendMessage(
        content: String,
        topicName: String?,
        streamId: Int
    ) : Int {
        return api.sendMessage(
            streamId = streamId,
            topicName = topicName ?: "(no topic)",
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
        topicName: String?,
        streamName: String,
    ) {
        val newList = messages.map { it.toMessageEntity(streamName) }
        val oldList = if (topicName == null) {
            dao.getMessagesInStream(streamName = streamName)
        } else {
            dao.getMessagesInTopic(topicName = topicName, streamName = streamName)
        }
        var finalList = (newList + oldList).sortedBy { it.id }
        if (finalList.size > CACHE_SIZE && topicName != null) {
            finalList = finalList.drop(finalList.size - CACHE_SIZE)
            dao.deleteAllInTopic(topicName)
        }
        dao.updateMessages(finalList)
    }

    companion object {
        private const val CACHE_SIZE = 50
    }
}