package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import javax.inject.Inject

class ChangeMessageTopicUseCase @Inject constructor(
    val repository: MessagesRepository
) {
    suspend fun invoke(messageId: Int, topicName: String) {
        repository.changeMessageTopic(messageId = messageId, topicName = topicName)
    }
}