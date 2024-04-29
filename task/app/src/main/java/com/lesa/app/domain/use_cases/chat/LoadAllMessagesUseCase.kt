package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.domain.model.Message
import javax.inject.Inject

class LoadAllMessagesUseCase @Inject constructor(
    private val repository: MessagesRepository
) {
    suspend fun invoke(streamName: String, topicName: String): List<Message> {
        return repository.getAllMessagesInStream(streamName = streamName, topicName = topicName)
    }
}