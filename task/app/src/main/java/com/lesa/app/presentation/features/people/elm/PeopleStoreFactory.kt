package com.lesa.app.presentation.features.people.elm

import com.lesa.app.presentation.utils.LceState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class PeopleStoreFactory @Inject constructor(private val actor: PeopleActor) {

    fun create() : Store<PeopleEvent, PeopleEffect, PeopleState> {
        return ElmStore(
            initialState = PeopleState(
                lceState = LceState.Loading,
                isSearching = false,
                users = listOf()
            ),
            reducer = PeopleReducer(),
            actor = actor
        )
    }
}