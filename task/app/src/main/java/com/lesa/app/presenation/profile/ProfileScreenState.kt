package com.lesa.app.presenation.profile

import com.lesa.app.domain.model.User

sealed interface ProfileScreenState {

    object Loading : ProfileScreenState

    object Error : ProfileScreenState

    class DataLoaded(val user: User) : ProfileScreenState
}