package com.lesa.app.di

import android.app.Application
import com.github.terrakok.cicerone.Router
import com.lesa.app.data.network.Api
import com.lesa.app.di.chat.ChatDeps
import com.lesa.app.di.people.PeopleDeps
import com.lesa.app.di.profile.ProfileDeps
import com.lesa.app.di.streams.StreamsDeps
import com.lesa.app.presentation.main.MainActivity
import com.lesa.app.presentation.main.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        NavigationModule::class
    ]
)
interface AppComponent : PeopleDeps, ProfileDeps, StreamsDeps, ChatDeps {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
    
    override val api: Api
    override val router: Router

    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
}

@Scope
annotation class AppScope