package com.lesa.app.presentation.profile

import com.lesa.app.presentation.profile.model.ProfileUi

sealed interface ProfileEvent {
    sealed interface Ui : ProfileEvent {
        data object Init : Ui
        data object ReloadProfile : Ui
    }

    sealed interface Internal : ProfileEvent {
        data class DataLoaded(val profileUi: ProfileUi) : Internal
        data object Error : Internal
    }
}