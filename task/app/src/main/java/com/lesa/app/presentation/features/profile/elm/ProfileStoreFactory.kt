package com.lesa.app.presentation.features.profile.elm

import com.lesa.app.presentation.utils.ScreenState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class ProfileStoreFactory(private val actor: ProfileActor) {

    fun create() : Store<ProfileEvent, ProfileEffect, ProfileState> {
        return ElmStore(
            initialState = ProfileState(profileUi = ScreenState.Loading),
            reducer = ProfileReducer(),
            actor = actor
        )
    }
}