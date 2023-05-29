package com.example.knittinghelper.presentation.profile.viewmodels

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knittinghelper.domain.model.Post
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.domain.use_cases.PostUseCases
import com.example.knittinghelper.domain.use_cases.UserUseCases
import com.example.knittinghelper.util.Response
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val userUseCases: UserUseCases,
    private val postUseCases: PostUseCases
) : ViewModel() {

    private val userId = auth.currentUser?.uid

    private val _getUserData = mutableStateOf<Response<User?>>(Response.Success(null))
    val getUserData : State<Response<User?>> = _getUserData

    private val _setUserData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val setUserData : State<Response<Boolean>> = _setUserData

    private val _getUserPostsData = mutableStateOf<Response<List<Post>?>>(Response.Success(null))
    val getUserPosts : State<Response<List<Post>?>> = _getUserPostsData

    private val _createPostData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val createPostData : State<Response<Boolean>> = _createPostData

    private val _deletePostData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val deletePostData : State<Response<Boolean>> = _deletePostData

    fun getUserInfo() {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.getUserDetails(userId).collect {
                    _getUserData.value = it
                }
            }
        }
    }

    fun setUserInfo(userName: String, bio: String) {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.setUserDetails(
                    userId = userId,
                    userName = userName,
                    bio = bio
                ).collect() {
                    _setUserData.value = it
                }
            }
        }
    }

    fun getUserPosts() {
        if(userId != null) {
            viewModelScope.launch {
                postUseCases.getUserPosts(userId).collect() {
                    _getUserPostsData.value = it
                }
            }
        }
    }

    fun createPost(
        userName: String,
        userPhotoUri: String,
        photoUris: List<Uri?>,
        text: String,
        needle: String
    ) {
        if(userId != null) {
            viewModelScope.launch {
                postUseCases.createPost(userId, userName, userPhotoUri, photoUris, text, needle).collect() {
                    _createPostData.value = it
                }
            }
        }
    }

    fun undoCreatePost() {
        _createPostData.value = Response.Success(false)
    }

    fun deletePost(
        postId: String
    ) {
        if(userId != null) {
            viewModelScope.launch {
                postUseCases.deletePost(postId).collect() {
                    _deletePostData.value = it
                }
            }
        }
    }
}