package com.lesa.app.data.local.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lesa.app.data.local.converters.ListStringConverter
import com.lesa.app.data.local.dao.StreamDao
import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.local.entities.StreamEntity
import com.lesa.app.data.local.entities.UserEntity

@TypeConverters(ListStringConverter::class)
@Database(
    entities = [
        UserEntity::class,
        StreamEntity::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun streamDao(): StreamDao
}