package com.example.knittinghelper.domain.use_cases.user

import com.example.knittinghelper.domain.repository.UserRepository
import javax.inject.Inject

class SetUserDetails @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String, userName: String, bio: String) =
        repository.setUserDetails(userId = userId, userName = userName, bio = bio)
}