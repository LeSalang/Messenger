package com.lesa.app.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadFileResponseApiDto(
    @SerialName("uri") val uri: String
)