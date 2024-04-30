package com.lesa.app.presentation.features.people.elm

import com.lesa.app.domain.model.User
import com.lesa.app.presentation.features.people.model.UserUi
import com.lesa.app.presentation.utils.LceState

data class PeopleState(
    val lceState: LceState<List<UserUi>>,
    val users: List<User>,
    val isSearching: Boolean
)