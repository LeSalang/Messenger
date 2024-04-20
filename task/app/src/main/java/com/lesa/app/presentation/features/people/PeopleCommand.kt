package com.lesa.app.presentation.features.people

sealed interface PeopleCommand {
    data object LoadData : PeopleCommand
}