package com.lesa.app.presentation.features.profile.elm

import com.lesa.app.domain.model.User

sealed interface ProfileEvent {
    sealed interface Ui : ProfileEvent {
        data object Init : Ui
        data object ReloadProfile : Ui
    }

    sealed interface Internal : ProfileEvent {
        data class DataLoaded(val profile: User) : Internal
        data object Error : Internal
    }
}