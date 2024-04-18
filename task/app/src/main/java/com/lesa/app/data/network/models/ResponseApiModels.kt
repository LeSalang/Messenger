package com.lesa.app.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseApiDto(
    @SerialName("id")
    val id: Int
)