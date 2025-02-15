package com.lesa.app.di

import android.content.Context
import androidx.room.Room
import com.lesa.app.data.local.dao.MessageDao
import com.lesa.app.data.local.dao.StreamDao
import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.local.databases.Database
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @AppScope
    @Provides
    fun provideDatabase(context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideUserDao(database: Database): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideStreamDao(database: Database): StreamDao {
        return database.streamDao()
    }

    @Provides
    fun provideMessageDao(database: Database): MessageDao {
        return database.messageDao()
    }

    companion object {
        const val DATABASE_NAME = "MAIN_DB"
    }
}