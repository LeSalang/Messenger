package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.domain.model.Message
import com.lesa.app.domain.model.MessageAnchor
import javax.inject.Inject

class LoadMessagesInTopicUseCase @Inject constructor(
    private val repository: MessagesRepository
) {
    suspend fun invoke(streamName: String, topicName: String, anchor: MessageAnchor): List<Message> {
        return repository.getMessagesInTopic(streamName = streamName, topicName = topicName, messageAnchor = anchor)
    }
}