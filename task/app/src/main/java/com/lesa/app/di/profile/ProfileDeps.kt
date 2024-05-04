package com.lesa.app.di.profile

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.network.Api
import kotlin.properties.Delegates.notNull

interface ProfileDeps {
    val api: Api
    val router: Router
    val userDao: UserDao
}

interface ProfileDepsProvider {
    val deps: ProfileDeps
    companion object : ProfileDepsProvider by ProfileDepsStore
}

object ProfileDepsStore : ProfileDepsProvider {
    override var deps: ProfileDeps by notNull()
}

internal class ProfileComponentViewModel : ViewModel() {
    val component = DaggerProfileComponent.builder()
        .deps(ProfileDepsProvider.deps)
        .build()
}