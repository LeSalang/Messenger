package com.lesa.app.profile

import com.lesa.app.model.User

sealed interface ProfileScreenState {

    object Loading : ProfileScreenState

    object Error : ProfileScreenState

    class DataLoaded(val user: User) : ProfileScreenState
}