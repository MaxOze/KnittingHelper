package com.example.knittinghelper.domain.repository

import android.net.Uri
import com.example.knittinghelper.domain.model.Part
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.util.Response
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getProject(userId: String, projectId: String): Flow<Response<Project>>

    fun getUserProjects(userId: String): Flow<Response<List<Project>>>

    fun createProject(
        userId: String,
        name: String,
        text: String,
        photoUri: Uri?,
        videoUri: String,
        needle: String,
        neededRow: Int): Flow<Response<Boolean>>

    fun deleteProject(userId: String, projectId: String): Flow<Response<Boolean>>

    fun updatePartProgress(
        userId: String,
        projectId: String,
        partId: String,
        oldRows: Int,
        addRows: Int,
        projectRows: Int
    ) : Flow<Response<Boolean>>

    fun getPart(userId: String, projectId: String, partId: String): Flow<Response<Part>>

    fun getProjectParts(userId: String, projectId: String): Flow<Response<List<Part>>>

    fun createPart(
        userId: String,
        projectId: String,
        name: String,
        text: String,
        needle: String,
        photoUri: Uri?,
        schemeUri: List<Uri?>,
        neededRow: Int,
        projectNeededRows: Int): Flow<Response<Boolean>>

    fun deletePart(
        userId: String,
        projectId: String,
        partId: String,
        rows: Int,
        neededRows: Int,
        projectRows: Int,
        projectNeededRows: Int): Flow<Response<Boolean>>
}