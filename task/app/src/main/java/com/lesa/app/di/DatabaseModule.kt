package com.lesa.app.di

import android.content.Context
import androidx.room.Room
import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.local.databases.Database
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideDao(database: Database): UserDao {
        return database.userDao()
    }

    companion object {
        const val DATABASE_NAME = "MAIN_DB"
    }
}