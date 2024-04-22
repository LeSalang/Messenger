package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository

class DeleteReactionUseCase(
    private val repository: MessagesRepository
) {
    suspend fun invoke(messageId: Int, emojiName: String) {
        return repository.deleteReaction(
            messageId = messageId,
            emojiName = emojiName
        )
    }
}