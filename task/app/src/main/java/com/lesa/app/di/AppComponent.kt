package com.lesa.app.di

import android.content.Context
import com.github.terrakok.cicerone.Router
import com.lesa.app.data.local.dao.MessageDao
import com.lesa.app.data.local.dao.StreamDao
import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.network.Api
import com.lesa.app.di.chat.ChatDeps
import com.lesa.app.di.people.PeopleDeps
import com.lesa.app.di.profile.ProfileDeps
import com.lesa.app.di.streams.StreamsDeps
import com.lesa.app.presentation.features.chat.message.MessageView
import com.lesa.app.presentation.main.MainActivity
import com.lesa.app.presentation.main.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@AppScope
@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class,
        NavigationModule::class
    ]
)
interface AppComponent : PeopleDeps, ProfileDeps, StreamsDeps, ChatDeps {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun baseUrl(baseUrl: String): Builder

        fun build(): AppComponent
    }
    
    override val api: Api
    override val router: Router
    override val userDao: UserDao
    override val streamDao: StreamDao
    override val messageDao: MessageDao

    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
    fun inject(messageView: MessageView)
}

@Scope
annotation class AppScope