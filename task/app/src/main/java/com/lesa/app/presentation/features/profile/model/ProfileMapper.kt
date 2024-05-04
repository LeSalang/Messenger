package com.lesa.app.presentation.features.profile.model

import com.lesa.app.domain.model.User

class ProfileMapper() {
    fun map(user: User) : ProfileUi {
        return ProfileUi(
            id = user.id,
            name = user.name,
            email = user.email,
            avatar = user.avatar
        )
    }
}