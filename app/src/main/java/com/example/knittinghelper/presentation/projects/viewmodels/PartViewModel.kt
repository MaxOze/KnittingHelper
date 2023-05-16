package com.example.knittinghelper.presentation.projects.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knittinghelper.domain.model.Part
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.domain.use_cases.ProjectUseCases
import com.example.knittinghelper.util.Response
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val auth: FirebaseAuth,
    private val projectUseCases: ProjectUseCases
) : ViewModel() {

    private val userId = auth.currentUser?.uid
    private val projectId: String = checkNotNull(savedStateHandle["projectId"])
    private val partId: String = checkNotNull(savedStateHandle["partId"])

    private val _getPartData = mutableStateOf<Response<Part?>>(Response.Success(null))
    val getPartData : State<Response<Part?>> = _getPartData

    fun getPartInfo() {
        if(userId != null) {
            viewModelScope.launch {
                projectUseCases.getPart(projectId, partId).collect {
                    _getPartData.value = it
                }
            }
        }
    }
}