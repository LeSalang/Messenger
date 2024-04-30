package com.lesa.app.presentation.features.people.elm

import com.lesa.app.domain.model.User

sealed interface PeopleEvent {
    sealed interface Ui : PeopleEvent {
        data object Init : Ui
        data object ReloadPeople : Ui
        data class Search(val query: String) : Ui
        data object OnSearchClicked : Ui
    }

    sealed interface Internal : PeopleEvent {
        data class DataLoaded(val users: List<User>) : Internal
        data object Error : Internal
    }
}