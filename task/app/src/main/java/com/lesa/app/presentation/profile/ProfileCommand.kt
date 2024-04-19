package com.lesa.app.presentation.profile

sealed interface ProfileCommand {
    data object LoadData : ProfileCommand
}