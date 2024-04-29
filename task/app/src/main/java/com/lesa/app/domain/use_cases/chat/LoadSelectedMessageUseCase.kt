package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.domain.model.Message
import javax.inject.Inject

class LoadSelectedMessageUseCase @Inject constructor(
    private val repository: MessagesRepository
) {
    suspend fun invoke(messageId: Int): Message {
        return repository.getMessage(messageId = messageId)
    }
}