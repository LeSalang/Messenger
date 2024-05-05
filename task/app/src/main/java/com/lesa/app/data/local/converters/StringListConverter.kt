package com.lesa.app.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StringListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            Json.decodeFromString<List<String>>(value)
        } catch (e: Exception) {
            listOf()
        }
    }
}