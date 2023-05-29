package com.example.knittinghelper.domain.repository

import android.net.Uri
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
        photoUri: Uri?,
        videoUri: String,
        needle: String,
        simpleProject: Boolean,
        neededRow: Int): Flow<Response<Boolean>>

    fun deleteProject(projectId: String): Flow<Response<Boolean>>

    fun updateSimpleProject(projectId: String, newRows: Int) : Flow<Response<Boolean>>
    fun updatePartProgress(
        projectId: String,
        partId: String,
        oldRows: Int,
        addRows: Int,
        projectRows: Int
    ) : Flow<Response<Boolean>>

    fun getPart(partId: String): Flow<Response<Part>>

    fun getProjectParts(projectId: String): Flow<Response<List<Part>>>

    fun createPart(
        projectId: String,
        name: String,
        text: String,
        needle: String,
        photoUri: Uri?,
        neededRow: Int,
        projectNeededRows: Int): Flow<Response<Boolean>>

    fun deletePart(
        projectId: String,
        partId: String,
        rows: Int,
        neededRows: Int,
        projectRows: Int,
        projectNeededRows: Int): Flow<Response<Boolean>>
}