package com.lesa.app.presenation.people

import com.lesa.app.domain.model.User

sealed interface PeopleScreenState {

    object Loading : PeopleScreenState

    object Error : PeopleScreenState

    class DataLoaded(val users: List<User>) : PeopleScreenState
}

val PeopleScreenState.users: List<User>
    get() = when (this) {
        is PeopleScreenState.DataLoaded -> this.users
        PeopleScreenState.Error -> emptyList()
        PeopleScreenState.Loading -> emptyList()
    }