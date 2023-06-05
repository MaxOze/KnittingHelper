package com.example.knittinghelper.domain.repository

import android.net.Uri
import android.provider.ContactsContract.DisplayPhoto
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.util.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserDetails(userId: String): Flow<Response<User>>
    fun getUsers(userName: String): Flow<Response<List<User>>>
    fun setUserDetails(userId: String, photo: Uri?, bio: String): Flow<Response<Boolean>>
    fun subscribe(userId: String, subUserId: String): Flow<Response<Boolean>>
    fun unSubscribe(userId: String, subUserId: String): Flow<Response<Boolean>>
    fun getUserSubscribers(userIds: List<String>): Flow<Response<List<User>>>
}