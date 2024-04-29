package com.lesa.app.di.chat

import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.data.repositories.MessagesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface ChatBindModule {

    @Binds
    fun bindChatRepository(impl: MessagesRepositoryImpl): MessagesRepository
}