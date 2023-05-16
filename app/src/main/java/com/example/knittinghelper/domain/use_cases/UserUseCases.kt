package com.example.knittinghelper.domain.use_cases

import com.example.knittinghelper.domain.repository.UserRepository
import javax.inject.Inject


data class UserUseCases(
    val getUserDetails: GetUserDetails,
    val setUserDetails: SetUserDetails
)

class GetUserDetails @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String) = repository.getUserDetails(userId = userId)
}

class SetUserDetails @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String, userName: String, bio: String) =
        repository.setUserDetails(userId = userId, userName = userName, bio = bio)
}