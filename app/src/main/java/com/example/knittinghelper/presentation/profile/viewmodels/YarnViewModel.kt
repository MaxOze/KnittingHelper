package com.example.knittinghelper.presentation.profile.viewmodels

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knittinghelper.domain.model.Yarn
import com.example.knittinghelper.domain.use_cases.YarnUseCases
import com.example.knittinghelper.util.Response
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YarnViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val yarnUseCases: YarnUseCases
) : ViewModel() {
    private val userId = auth.currentUser?.uid

    private val _getUserYarnsData = mutableStateOf<Response<List<Yarn>?>>(Response.Success(null))
    val getUserYarnsData : State<Response<List<Yarn>?>> = _getUserYarnsData

    private val _createYarnData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val createYarnData : State<Response<Boolean>> = _createYarnData

    private val _updateYarnData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val updateYarnData : State<Response<Boolean>> = _updateYarnData

    private val _deleteYarnData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val deleteYarnData : State<Response<Boolean>> = _deleteYarnData

    fun getUserYarns() {
        if(userId != null) {
            viewModelScope.launch {
                yarnUseCases.getUserYarns(userId).collect {
                    _getUserYarnsData.value = it
                }
            }
        }
    }

    fun createYarn(
        color: String,
        material: String,
        length: Int,
        weight: Int,
        amount: Int,
        photoUri: Uri?
    ) {
        if(userId != null) {
            viewModelScope.launch {
                yarnUseCases.createYarn(userId, color, material, length, weight, amount, photoUri).collect {
                    _createYarnData.value = it
                }
            }
        }
    }

    fun updateYarn(yarnId: String, text: String, count: Int) {
        if(userId != null) {
            viewModelScope.launch {
                yarnUseCases.updateYarn(yarnId, text, count).collect {
                    _updateYarnData.value = it
                }
            }
        }
    }

    fun deleteYarn(yarnId: String) {
        if(userId != null) {
            viewModelScope.launch {
                yarnUseCases.deleteYarn(yarnId).collect {
                    _deleteYarnData.value = it
                }
            }
        }
    }
}