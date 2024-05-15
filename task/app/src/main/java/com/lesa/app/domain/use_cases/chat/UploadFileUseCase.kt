package com.lesa.app.domain.use_cases.chat

import android.content.ContentResolver
import android.net.Uri
import com.lesa.app.data.repositories.MessagesRepository
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend fun invoke(name: String, uri: Uri, contentResolver: ContentResolver) : String {
        return messagesRepository.uploadFile(name = name, uri = uri, contentResolver = contentResolver)
    }
}