package com.lesa.app.domain.use_cases.people

import com.lesa.app.data.repositories.UserRepository
import com.lesa.app.domain.model.User
import javax.inject.Inject

class LoadCachedPeopleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun invoke() : List<User> {
        return userRepository.getCachedUsers()
    }
}