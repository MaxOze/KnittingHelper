package com.example.knittinghelper.domain.use_cases

import android.net.Uri
import com.example.knittinghelper.domain.repository.PostRepository
import javax.inject.Inject

data class PostUseCases(
    val getUserPosts: GetUserPosts,
    val createPost: CreatePost,
    val deletePost: DeletePost,
    val getPostsFeed: GetPostsFeed
)

class GetUserPosts @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(userId: String) = repository.getUserPosts(userId)
}

class CreatePost @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(
        userId: String,
        userName: String,
        userPhotoUri: String,
        photoUris: List<Uri?>,
        text: String,
        needle: String
    ) = repository.createPost(userId, userName, userPhotoUri, photoUris, text, needle)
}

class DeletePost @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(postId: String) = repository.deletePost(postId)
}

class GetPostsFeed @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(userId: String, count: Int) = repository.getPostsFeed(userId, count)
}