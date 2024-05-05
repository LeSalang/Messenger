package com.lesa.app.data.local.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lesa.app.data.local.converters.ReactionListConverter
import com.lesa.app.data.local.converters.StringListConverter
import com.lesa.app.data.local.dao.MessageDao
import com.lesa.app.data.local.dao.StreamDao
import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.local.entities.MessageEntity
import com.lesa.app.data.local.entities.StreamEntity
import com.lesa.app.data.local.entities.UserEntity

@TypeConverters(
    StringListConverter::class,
    ReactionListConverter::class
)
@Database(
    entities = [
        UserEntity::class,
        StreamEntity::class,
        MessageEntity::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun streamDao(): StreamDao
    abstract fun messageDao(): MessageDao
}