package com.example.knittinghelper.presentation.social.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knittinghelper.domain.model.Post
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.domain.use_cases.PostUseCases
import com.example.knittinghelper.domain.use_cases.UserUseCases
import com.example.knittinghelper.util.Response
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val auth: FirebaseAuth,
    private val postUseCases: PostUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val userId = auth.currentUser?.uid
    private val userProfileId: String = checkNotNull(savedStateHandle["userId"])
    var isSub: String = checkNotNull(savedStateHandle["sub"])

    private val _getUserProfileData = mutableStateOf<Response<User?>>(Response.Success(null))
    val getUserProfileData : State<Response<User?>> = _getUserProfileData

    private val _getUserPostsData = mutableStateOf<Response<List<Post>?>>(Response.Success(null))
    val getUserPostsData : State<Response<List<Post>?>> = _getUserPostsData

    fun getUserInfo() {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.getUserDetails(userProfileId).collect {
                    _getUserProfileData.value = it
                }
            }
        }
    }

    fun getUserPosts() {
        if(userId != null) {
            viewModelScope.launch {
                postUseCases.getUserPosts(userProfileId).collect() {
                    _getUserPostsData.value = it
                }
            }
        }
    }

    fun subscribe(subUserId: String) {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.subscribe(userId, subUserId).collect {

                }
            }
        }
    }

    fun unsubscribe(subUserId: String) {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.unSubscribe(userId, subUserId).collect {

                }
            }
        }
    }
}