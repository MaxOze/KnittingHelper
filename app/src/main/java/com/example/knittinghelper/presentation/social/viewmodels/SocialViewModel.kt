package com.example.knittinghelper.presentation.social.viewmodels

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knittinghelper.domain.model.Post
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.domain.use_cases.PostUseCases
import com.example.knittinghelper.domain.use_cases.ProjectUseCases
import com.example.knittinghelper.domain.use_cases.UserUseCases
import com.example.knittinghelper.util.Response
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val postUseCases: PostUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val userId = auth.currentUser?.uid
    private var count: Long = 0

    private val _getUserData = mutableStateOf<Response<User?>>(Response.Success(null))
    val getUserData : State<Response<User?>> = _getUserData

    private val _getUsersData = mutableStateOf<Response<List<User>>>(Response.Success(emptyList()))
    val getUsersData : State<Response<List<User>>> = _getUsersData

    private val _getPostsFeedData = mutableStateOf<Response<List<Post>?>>(Response.Success(null))
    val getPostsFeedData : State<Response<List<Post>?>> = _getPostsFeedData

    fun getUserInfo() {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.getUserDetails(userId).collect {
                    _getUserData.value = it
                }
            }
        }
    }

    fun getUsers(userName: String) {
        if (userId != null) {
            viewModelScope.launch {
                userUseCases.getUsers(userName).collect {
                    _getUsersData.value = it
                }
            }
        }
    }

    fun getUserUndo() {
        _getUsersData.value = Response.Success(emptyList())
    }

    fun GetPostsFeed() {
        count++
        if (userId != null) {
            viewModelScope.launch {
                val user = getUserData.value as Response.Success<User?>
                if (user.data != null) {
                    postUseCases.getPostsFeed(user.data.following, count).collect {
                        _getPostsFeedData.value = it
                    }
                }
            }
        }
    }

    fun undo() {
        _getPostsFeedData.value = Response.Success(null)
    }
}