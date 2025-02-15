package com.lesa.app.domain.use_cases.people

import com.lesa.app.data.repositories.UserRepository
import com.lesa.app.domain.model.User
import javax.inject.Inject

class LoadPeopleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke() : List<User> {
        return userRepository.getUpdatedUsers()
    }
}