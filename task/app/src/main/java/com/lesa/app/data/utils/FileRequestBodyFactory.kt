package com.lesa.app.data.utils

import android.content.Context
import android.net.Uri
import com.lesa.app.presentation.utils.InputStreamRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import javax.inject.Inject

class FileRequestBodyFactory @Inject constructor(
    applicationContext: Context
) {
    private val contentResolver = applicationContext.contentResolver

    fun createRequestBody(uri: Uri, name: String): MultipartBody {
        val mediaType = contentResolver
            .getType(uri)
            ?.toMediaTypeOrNull()
            ?: throw IllegalArgumentException("unsupported media type")
        val requestBody = InputStreamRequestBody(
            contentType = mediaType,
            contentResolver = contentResolver,
            uri = uri
        )
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(
            name = name,
            filename = name + "." + mediaType.subtype,
            body = requestBody
        ).build()
        return multipartBody
    }
}