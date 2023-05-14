package com.example.knittinghelper.domain.use_cases.user

import com.example.knittinghelper.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetails @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String) = repository.getUserDetails(userId = userId)
}