package com.lesa.app

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import com.lesa.app.api.Api

internal class App : Application() {
    private val cicerone = Cicerone.create()
    val router
        get() = cicerone.router
    val navigatorHolder
        get() = cicerone.getNavigatorHolder()

    lateinit var api: Api
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(context = this)
        INSTANCE = this
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }
}