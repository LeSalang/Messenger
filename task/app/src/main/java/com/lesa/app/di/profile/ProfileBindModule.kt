package com.lesa.app.di.profile

import com.lesa.app.data.repositories.UserRepository
import com.lesa.app.data.repositories.UserRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface ProfileBindModule {

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}