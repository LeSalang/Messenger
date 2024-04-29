package com.lesa.app

import android.app.Application
import com.lesa.app.di.AppComponent
import com.lesa.app.di.DaggerAppComponent
import com.lesa.app.di.chat.ChatDepsStore
import com.lesa.app.di.people.PeopleDepsStore
import com.lesa.app.di.profile.ProfileDepsStore
import com.lesa.app.di.streams.StreamsDepsStore

internal class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = buildComponent()
        setUpModulesDeps(appComponent)
        INSTANCE = this
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder().application(this).build()
    }

    private fun setUpModulesDeps(appComponent: AppComponent) {
        ChatDepsStore.deps = appComponent
        PeopleDepsStore.deps = appComponent
        ProfileDepsStore.deps = appComponent
        StreamsDepsStore.deps = appComponent
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }
}