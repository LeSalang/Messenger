package com.lesa.app.di.streams

import com.lesa.app.presentation.features.streams.StreamsFragment
import dagger.Component
import javax.inject.Scope

@StreamsScope
@Component(
    modules = [
        StreamsBindModule::class
    ],
    dependencies = [StreamsDeps::class]
)
interface StreamsComponent {

    fun inject(fragment: StreamsFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: StreamsDeps): Builder

        fun build(): StreamsComponent
    }
}

@Scope
annotation class StreamsScope