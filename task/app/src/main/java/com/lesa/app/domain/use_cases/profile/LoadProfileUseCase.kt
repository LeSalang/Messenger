package com.lesa.app.domain.use_cases.profile

import com.lesa.app.data.repositories.UserRepository
import com.lesa.app.domain.model.User

class LoadProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend fun invoke() : User {
        return userRepository.getOwnUser()
    }
}