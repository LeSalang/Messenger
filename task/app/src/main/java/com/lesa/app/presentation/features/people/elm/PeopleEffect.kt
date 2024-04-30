package com.lesa.app.presentation.features.people.elm

import com.lesa.app.presentation.features.people.model.UserUi

sealed interface PeopleEffect {
    data class OpenProfile(val user: UserUi) : PeopleEffect
}