package com.lesa.app.di.profile

import com.lesa.app.presentation.features.profile.ProfileFragment
import dagger.Component
import javax.inject.Scope

@ProfileScope
@Component(
    modules = [
        ProfileBindModule::class
    ],
    dependencies = [ProfileDeps::class]
)
interface ProfileComponent {
    fun inject(fragment: ProfileFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: ProfileDeps): Builder

        fun build(): ProfileComponent
    }
}

@Scope
annotation class ProfileScope