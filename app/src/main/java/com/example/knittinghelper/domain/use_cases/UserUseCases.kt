package com.example.knittinghelper.domain.use_cases

import com.example.knittinghelper.domain.repository.UserRepository
import javax.inject.Inject


data class UserUseCases(
    val getUserDetails: GetUserDetails,
    val setUserDetails: SetUserDetails,
    val subscribe: Subscribe,
    val unSubscribe: UnSubscribe,
    val getUserSubscribers: GetUserSubscribers
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

class Subscribe @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String, userIds: List<String>, subUserId: String) = repository.subscribe(userId, userIds, subUserId)
}

class UnSubscribe @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String, userIds: List<String>, subUserId: String) = repository.unSubscribe(userId, userIds, subUserId)
}

class GetUserSubscribers @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userIds: List<String>) = repository.getUserSubscribers(userIds)
}