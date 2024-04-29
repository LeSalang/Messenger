package com.lesa.app.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {
    private val cicerone = Cicerone.create()

    @Provides
    fun provideMainRouter(): Router {
        return cicerone.router
    }

    @Provides
    fun provideMainNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }
}