package com.example.knittinghelper.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knittinghelper.domain.model.User
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
) : ViewModel() {

    private val userId = auth.currentUser?.uid

    private val _getUserData = mutableStateOf<Response<User?>>(Response.Success(null))
    val getUserData : State<Response<User?>> = _getUserData

    private val _setUserData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val setUserData : State<Response<Boolean>> = _setUserData


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
}