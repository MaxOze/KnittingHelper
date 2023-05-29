package com.example.knittinghelper.domain.repository

import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.util.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserDetails(userId: String): Flow<Response<User>>
    fun setUserDetails(userId: String, userName: String, bio: String): Flow<Response<Boolean>>
    fun subscribe(userId: String, userIds: List<String>, subUserId: String): Flow<Response<Boolean>>
    fun unSubscribe(userId: String, userIds: List<String>, subUserId: String): Flow<Response<Boolean>>
    fun getUserSubscribers(userIds: List<String>): Flow<Response<List<User>>>
}