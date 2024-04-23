package com.lesa.app.presentation.features.people.elm

sealed interface PeopleCommand {
    data object LoadData : PeopleCommand
    data class Search(val query: String) : PeopleCommand
}