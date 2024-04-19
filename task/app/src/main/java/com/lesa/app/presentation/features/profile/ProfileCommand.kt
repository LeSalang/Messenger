package com.lesa.app.presentation.features.profile

sealed interface ProfileCommand {
    data object LoadData : ProfileCommand
}