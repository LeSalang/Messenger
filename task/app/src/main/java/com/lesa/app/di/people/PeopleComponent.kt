package com.lesa.app.di.people

import com.lesa.app.presentation.features.people.PeopleFragment
import com.lesa.app.presentation.features.user.UserFragment
import dagger.Component
import javax.inject.Scope

@PeopleScope
@Component(
    modules = [PeopleBindModule::class],
    dependencies = [PeopleDeps::class]
)
interface PeopleComponent {
    fun inject(fragment: PeopleFragment)
    fun inject(fragment: UserFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: PeopleDeps): Builder

        fun build(): PeopleComponent
    }
}

@Scope
annotation class PeopleScope