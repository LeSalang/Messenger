package com.lesa.app.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class SubscriptionsQuery(
    @SerialName("name") val name: String
) {
    companion object {
        fun createQuery(streamName: String): String {
            val query = listOf(
                SubscriptionsQuery(name = streamName),
            )
            return Json.encodeToString(value = query)
        }
    }
}