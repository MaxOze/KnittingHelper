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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val projectUseCases: ProjectUseCases
) : ViewModel() {

    private val userId = auth.currentUser?.uid

    private val _getUserProjectsData = mutableStateOf<Response<List<Project>>>(Response.Success(emptyList()))
    val getUserProjectsData : State<Response<List<Project>>> = _getUserProjectsData

    private val _createProjectData = mutableStateOf<Response<Boolean>>(Response.Success(false))
    val createProjectData : State<Response<Boolean>> = _createProjectData

    fun getUserProjects() {
        if(userId != null) {
            viewModelScope.launch {
                projectUseCases.getUserProjects(userId).collect {
                    _getUserProjectsData.value = it
                }
            }
        }
    }

    fun createProject(
        name: String,
        text: String,
        photoUrl: String,
        videoUrl: String,
        needle: String
    ) {
        if(userId != null) {
            viewModelScope.launch {
                projectUseCases.createProject(userId, name, text, photoUrl, videoUrl, needle).collect {
                    _createProjectData.value = it
                }
            }
        }
    }

}