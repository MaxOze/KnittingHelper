package com.example.knittinghelper.domain.repository

import android.net.Uri
import com.example.knittinghelper.domain.model.Post
import com.example.knittinghelper.util.Response
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getUserPosts(userId: String): Flow<Response<List<Post>>>
    fun createPost(
        userId: String,
        userName: String,
        userPhotoUri: String,
        photoUris: List<Uri?>,
        text: String,
        needle: String): Flow<Response<Boolean>>
    fun deletePost(postId: String): Flow<Response<Boolean>>
    fun getPostsFeed(userId: String, count: Int): Flow<Response<List<Post>>>
}