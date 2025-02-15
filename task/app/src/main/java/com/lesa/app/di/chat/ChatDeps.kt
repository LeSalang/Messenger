package com.lesa.app.di.chat

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.lesa.app.data.local.dao.MessageDao
import com.lesa.app.data.network.Api
import kotlin.properties.Delegates.notNull

interface ChatDeps {
    val api: Api
    val router: Router
    val messageDao: MessageDao
    val context: Context
}

interface ChatDepsProvider {
    val deps: ChatDeps
    companion object : ChatDepsProvider by ChatDepsStore
}

object ChatDepsStore : ChatDepsProvider {
    override var deps: ChatDeps by notNull()
}

internal class ChatComponentViewModel : ViewModel() {
    val component = DaggerChatComponent.builder()
        .deps(ChatDepsProvider.deps)
        .build()
}