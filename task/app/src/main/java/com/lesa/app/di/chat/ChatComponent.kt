package com.lesa.app.di.chat

import com.lesa.app.presentation.features.chat.ChatFragment
import dagger.Component
import javax.inject.Scope

@ChatScope
@Component(
    modules = [
        ChatBindModule::class
    ],
    dependencies = [ChatDeps::class]
)
interface ChatComponent {
    fun inject(fragment: ChatFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: ChatDeps): Builder
        fun build(): ChatComponent
    }
}

@Scope
annotation class ChatScope