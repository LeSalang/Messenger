package com.lesa.app.presentation.features.people.elm

import com.lesa.app.presentation.features.people.model.UserUi

sealed interface PeopleEvent {
    sealed interface Ui : PeopleEvent {
        data object Init : Ui
        data object ReloadPeople : Ui
    }

    sealed interface Internal : PeopleEvent {
        data class DataLoaded(val userUiList: List<UserUi>) : Internal
        data object Error : Internal
    }
}