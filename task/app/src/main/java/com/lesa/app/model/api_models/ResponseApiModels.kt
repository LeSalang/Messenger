package com.lesa.app.model.api_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseApiDto(
    @SerialName("id")
    val id: Int
)