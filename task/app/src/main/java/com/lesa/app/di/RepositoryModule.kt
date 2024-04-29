package com.lesa.app.di

import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.data.repositories.MessagesRepositoryImpl
import com.lesa.app.data.repositories.StreamsRepository
import com.lesa.app.data.repositories.StreamsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun bindMessagesRepository(impl: MessagesRepositoryImpl): MessagesRepository

    @Binds
    fun bindStreamsRepository(impl: StreamsRepositoryImpl): StreamsRepository
}