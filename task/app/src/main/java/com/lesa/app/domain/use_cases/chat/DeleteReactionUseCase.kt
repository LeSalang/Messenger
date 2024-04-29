package com.lesa.app.domain.use_cases.chat

import com.lesa.app.data.repositories.MessagesRepository
import javax.inject.Inject

class DeleteReactionUseCase @Inject constructor(
    private val repository: MessagesRepository
) {
    suspend fun invoke(messageId: Int, emojiName: String) {
        return repository.deleteReaction(
            messageId = messageId,
            emojiName = emojiName
        )
    }
}