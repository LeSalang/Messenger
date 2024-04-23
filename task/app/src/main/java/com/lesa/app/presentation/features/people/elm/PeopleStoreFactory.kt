package com.lesa.app.presentation.features.people.elm

import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class PeopleStoreFactory(private val actor: PeopleActor) {

    fun create() : Store<PeopleEvent, PeopleEffect, PeopleState> {
        return ElmStore(
            initialState = PeopleState(
                peopleUi = ScreenState.Loading,
                isSearching = false
            ),
            reducer = PeopleReducer(),
            actor = actor
        )
    }
}