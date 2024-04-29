package com.lesa.app.di.streams

import com.lesa.app.data.repositories.StreamsRepository
import com.lesa.app.data.repositories.StreamsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface StreamsBindModule {

    @Binds
    fun bindStreamsRepository(impl: StreamsRepositoryImpl): StreamsRepository
}