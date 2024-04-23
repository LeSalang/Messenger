package com.lesa.app.presentation.features.people.elm

import com.lesa.app.presentation.features.people.model.UserUi
import com.lesa.app.presentation.utils.ScreenState

data class PeopleState(
    val peopleUi: ScreenState<List<UserUi>>,
    val isSearching: Boolean
)