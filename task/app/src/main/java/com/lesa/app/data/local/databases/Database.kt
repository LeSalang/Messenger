package com.lesa.app.data.local.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
}