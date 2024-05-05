package com.lesa.app.di.streams

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.lesa.app.data.local.dao.StreamDao
import com.lesa.app.data.network.Api
import kotlin.properties.Delegates.notNull

interface StreamsDeps {
    val api: Api
    val router: Router
    val streamDao: StreamDao
}

interface StreamsDepsProvider {
    val deps: StreamsDeps
    companion object : StreamsDepsProvider by StreamsDepsStore
}

object StreamsDepsStore : StreamsDepsProvider {
    override var deps: StreamsDeps by notNull()
}

internal class StreamsComponentViewModel : ViewModel() {
    val component = DaggerStreamsComponent.builder()
        .deps(StreamsDepsProvider.deps)
        .build()
}