package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    val repository: MessagesRepository
) {
    suspend fun invoke(messageId: Int) {
        repository.deleteMessage(messageId = messageId)
    }
}