package com.example.knittinghelper.domain.use_cases

import android.net.Uri
import com.example.knittinghelper.domain.repository.UserRepository
import javax.inject.Inject


data class UserUseCases(
    val getUserDetails: GetUserDetails,
    val getUsers: GetUsers,
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

class GetUsers @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userName: String) = repository.getUsers(userName = userName)
}

class SetUserDetails @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String, photoUri: Uri?, bio: String) =
        repository.setUserDetails(userId = userId, photo = photoUri, bio = bio)
}

class Subscribe @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String, subUserId: String) = repository.subscribe(userId, subUserId)
}

class UnSubscribe @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String, subUserId: String) = repository.unSubscribe(userId, subUserId)
}

class GetUserSubscribers @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userIds: List<String>) = repository.getUserSubscribers(userIds)
}