package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository

class SendMessageUseCase(
    private val repository: MessagesRepository
) {
    suspend fun invoke(content: String, topicName: String, streamId: Int) : Int {
        return repository.sendMessage(content = content, topicName = topicName, streamId = streamId)
    }
}