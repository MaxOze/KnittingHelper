package com.example.knittinghelper.presentation.profile.viewmodels

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
class SubsViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val userUseCases: UserUseCases,
) : ViewModel() {

    private val userId = auth.currentUser?.uid

    private val _getUserData = mutableStateOf<Response<User?>>(Response.Success(null))
    val getUserData : State<Response<User?>> = _getUserData

    private val _getUserSubsData = mutableStateOf<Response<List<User>?>>(Response.Success(null))
    val getUserSubsData : State<Response<List<User>?>> = _getUserSubsData

    private val _subscribeData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val subscribeData : State<Response<Boolean>> = _subscribeData

    private val _unsubscribeData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val unsubscribeData : State<Response<Boolean>> = _unsubscribeData


    fun getUser() {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.getUserDetails(userId).collect {
                    _getUserData.value = it
                }
            }
        }
    }

    fun getUserSubs(userIds: List<String>) {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.getUserSubscribers(userIds).collect {
                    _getUserSubsData.value = it
                }
            }
        }
    }

    fun subscribe(subUserId: String) {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.subscribe(userId, subUserId).collect {
                    _unsubscribeData.value = it
                }
            }
        }
    }

    fun unsubscribe(subUserId: String) {
        if(userId != null) {
            viewModelScope.launch {
                userUseCases.unSubscribe(userId, subUserId).collect {
                    _unsubscribeData.value = it
                }
            }
        }
    }
}