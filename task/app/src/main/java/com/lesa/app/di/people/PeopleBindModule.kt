package com.lesa.app.di.people

import com.lesa.app.data.repositories.UserRepository
import com.lesa.app.data.repositories.UserRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface PeopleBindModule {

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}