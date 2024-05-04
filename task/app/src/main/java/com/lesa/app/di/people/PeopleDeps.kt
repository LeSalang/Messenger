package com.lesa.app.di.people

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.lesa.app.data.local.dao.UserDao
import com.lesa.app.data.network.Api
import kotlin.properties.Delegates.notNull

interface PeopleDeps {
    val api: Api
    val router: Router
    val userDao: UserDao
}

interface PeopleDepsProvider {
    val deps: PeopleDeps
    companion object : PeopleDepsProvider by PeopleDepsStore
}

object PeopleDepsStore : PeopleDepsProvider {
    override var deps: PeopleDeps by notNull()
}

internal class PeopleComponentViewModel : ViewModel() {
    val component = DaggerPeopleComponent.builder()
        .deps(PeopleDepsProvider.deps)
        .build()
}