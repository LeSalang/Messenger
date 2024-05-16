package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.domain.model.Message
import javax.inject.Inject

class LoadAllCachedMessagesUseCase @Inject constructor(
    private val repository: MessagesRepository
) {
    suspend fun invoke(topicName: String, streamName: String) : List<Message> {
        return repository.getAllCachedMessagesInTopic(
            topicName = topicName,
            streamName = streamName
        )
    }
}