package com.example.knittinghelper.presentation.profile.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knittinghelper.domain.model.Needle
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.domain.model.Yarn
import com.example.knittinghelper.domain.use_cases.NeedleUseCases
import com.example.knittinghelper.util.Response
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NeedleViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val needleUseCases: NeedleUseCases
) : ViewModel() {

    private val userId = auth.currentUser?.uid

    private val _getUserNeedlesData = mutableStateOf<Response<List<Needle>?>>(Response.Success(null))
    val getUserNeedlesData : State<Response<List<Needle>?>> = _getUserNeedlesData

    private val _createNeedleData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val createNeedleData : State<Response<Boolean>> = _createNeedleData

    private val _deleteNeedleData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val deleteNeedleData : State<Response<Boolean>> = _deleteNeedleData

    fun getUserNeedles() {
        if(userId != null) {
            viewModelScope.launch {
                needleUseCases.getUserNeedles(userId).collect {
                    _getUserNeedlesData.value = it
                }
            }
        }
    }

    fun createNeedle(type: String, thickness: Float) {
        if(userId != null) {
            viewModelScope.launch {
                needleUseCases.createNeedle(userId, type, thickness).collect {
                    _createNeedleData.value = it
                }
            }
        }
    }

    fun createUndo() {
        _createNeedleData.value = Response.Success(false)
    }

    fun deleteNeedle(needleId: String) {
        if(userId != null) {
            viewModelScope.launch {
                needleUseCases.deleteNeedle(needleId).collect {
                    _deleteNeedleData.value = it
                }
            }
        }
    }

    fun deleteOk() {
        _deleteNeedleData.value = Response.Success(false)
    }
}