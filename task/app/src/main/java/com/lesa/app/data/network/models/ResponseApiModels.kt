package com.lesa.app.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponseApiDto(
    @SerialName("id") val id: Int
)

@Serializable
data class UriResponseApiDto(
    @SerialName("uri") val uri: String
)