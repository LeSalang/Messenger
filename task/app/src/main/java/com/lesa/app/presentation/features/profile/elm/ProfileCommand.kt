package com.lesa.app.presentation.features.profile.elm

sealed interface ProfileCommand {
    data object LoadData : ProfileCommand
}