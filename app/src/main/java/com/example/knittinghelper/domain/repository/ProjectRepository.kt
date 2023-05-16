package com.example.knittinghelper.domain.repository

import com.example.knittinghelper.domain.model.Part
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.util.Response
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getProject(projectId: String): Flow<Response<Project>>
    fun getUserProjects(userId: String): Flow<Response<List<Project>>>
    fun createProject(
        userId: String,
        name: String,
        text: String,
        photoUrl: String,
        videoUrl: String,
        needle: String): Flow<Response<Boolean>>
    fun getPart(projectId: String, partId: String): Flow<Response<Part>>
    fun getProjectParts(projectId: String): Flow<Response<List<Part>>>
    fun createPart(
        projectId: String,
        name: String,
        text: String,
        needle: String,
        schemeUrls: List<String>,
        neededRow: Int): Flow<Response<Boolean>>
}