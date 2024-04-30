package com.lesa.app.presentation.features.people.elm

sealed interface PeopleCommand {
    data object LoadUsers : PeopleCommand
}