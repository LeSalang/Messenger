package com.lesa.app.data.local.converters

import androidx.room.TypeConverter
import com.lesa.app.data.local.entities.ReactionEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReactionListConverter {
    @TypeConverter
    fun fromReactionList(value: List<ReactionEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toReactionList(value: String): List<ReactionEntity> {
        return try {
            Json.decodeFromString<List<ReactionEntity>>(value)
        } catch (e: Exception) {
            listOf()
        }
    }
}