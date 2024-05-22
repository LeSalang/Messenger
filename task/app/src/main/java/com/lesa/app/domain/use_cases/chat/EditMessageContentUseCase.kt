package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import javax.inject.Inject

class EditMessageContentUseCase @Inject constructor(
    private val repository: MessagesRepository
) {
    suspend fun invoke(messageId: Int, content: String) {
        repository.editMessageContent(messageId, content)
    }
}