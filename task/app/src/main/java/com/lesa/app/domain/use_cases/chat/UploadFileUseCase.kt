package com.lesa.app.domain.use_cases.chat

import android.net.Uri
import com.lesa.app.data.repositories.MessagesRepository
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend fun invoke(name: String, uri: Uri) : String {
        return messagesRepository.uploadFile(name = name, uri = uri)
    }
}