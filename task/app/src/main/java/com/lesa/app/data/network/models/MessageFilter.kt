package com.lesa.app.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class MessageFilter(
    @SerialName("operator") val operator: String,
    @SerialName("operand") val operand: String
) {
    companion object {
        fun createNarrow(streamName: String, topicName: String): String {
            val narrow = listOf(
                MessageFilter(operator = "stream", operand = streamName),
                MessageFilter(operator = "topic", operand = topicName)
            )
            return Json.encodeToString(value = narrow)
        }
    }
}

