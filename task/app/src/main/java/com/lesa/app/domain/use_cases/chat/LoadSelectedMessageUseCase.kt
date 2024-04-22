package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.domain.model.Message

class LoadSelectedMessageUseCase(
    private val repository: MessagesRepository
) {
    suspend fun invoke(messageId: Int): Message {
        return repository.getMessage(messageId = messageId)
    }
}