package com.example.knittinghelper.presentation.projects.viewmodels

import android.net.Uri
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
class ProjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val auth: FirebaseAuth,
    private val projectUseCases: ProjectUseCases
) : ViewModel() {

    private val userId = auth.currentUser?.uid
    private val projectId: String = checkNotNull(savedStateHandle["projectId"])

    private val _getProjectData = mutableStateOf<Response<Project?>>(Response.Success(null))
    val getProjectData : State<Response<Project?>> = _getProjectData

    private val _getProjectPartsData = mutableStateOf<Response<List<Part>>>(Response.Success(emptyList()))
    val getProjectPartsData : State<Response<List<Part>>> = _getProjectPartsData

    private val _createPartData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val createPartData : State<Response<Boolean>> = _createPartData

    private val _deletePartData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val deletePartData : State<Response<Boolean>> = _deletePartData

    fun getProjectInfo() {
        if(userId != null) {
            viewModelScope.launch {
                projectUseCases.getProject(projectId).collect {
                    _getProjectData.value = it
                }
            }
        }
    }

    fun getProjectParts() {
        if(userId != null) {
            viewModelScope.launch {
                projectUseCases.getProjectParts(projectId).collect {
                    _getProjectPartsData.value = it
                }
            }
        }
    }

    fun createPart(
        name: String,
        text: String,
        needle: String,
        photoUri: Uri?,
        neededRow: Int,
        projectRows: Int
    ) {
        if(userId != null) {
            viewModelScope.launch {
                projectUseCases.createPart(
                    projectId,
                    name,
                    text,
                    needle,
                    photoUri,
                    neededRow,
                    projectRows
                ).collect {
                    _createPartData.value = it
                }
            }
        }
    }

    fun deletePart(part: Part, project: Project) {
        if(userId != null) {
            viewModelScope.launch {
                projectUseCases.deletePart(
                    projectId,
                    part.partId,
                    part.countRow,
                    part.neededRow,
                    project.countRows,
                    project.neededRows
                ).collect {
                    _deletePartData.value = it
                }
            }
        }
    }

    fun deleteOk() {
        _deletePartData.value = Response.Success(false)
    }
}