package com.example.knittinghelper.domain.use_cases

import android.net.Uri
import com.example.knittinghelper.domain.repository.ProjectRepository
import javax.inject.Inject

data class ProjectUseCases(
    val getProject: GetProject,
    val getUserProjects: GetUserProjects,
    val createProject: CreateProject,
    val deleteProject: DeleteProject,
    val getPart: GetPart,
    val getProjectParts: GetProjectParts,
    val createPart: CreatePart,
    val updatePartProgress: UpdatePartProgress,
    val deletePart: DeletePart
)

class GetProject @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(userId: String, projectId: String) = repository.getProject(userId, projectId)
}


class GetUserProjects @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(userId: String) = repository.getUserProjects(userId)
}

class CreateProject @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(
        userId: String,
        name: String,
        text: String,
        photoUri: Uri?,
        videoUri: String,
        needle: String,
        neededRow: Int
    ) = repository.createProject(userId, name, text, photoUri, videoUri, needle, neededRow)
}

class DeleteProject @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(userId: String, projectId: String) = repository.deleteProject(userId, projectId)
}

class GetPart @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(userId: String, projectId: String, partId: String) = repository.getPart(userId, projectId, partId)
}


class GetProjectParts @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(userId: String, projectId: String) = repository.getProjectParts(userId, projectId)
}

class CreatePart @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(
        userId: String,
        projectId: String,
        name: String,
        text: String,
        needle: String,
        photoUri: Uri?,
        schemeUri: List<Uri?>,
        neededRow: Int,
        projectNeededRows: Int
    ) = repository.createPart(userId, projectId, name, text, needle, photoUri, schemeUri, neededRow, projectNeededRows)
}

class UpdatePartProgress @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(
        userId: String,
        projectId: String,
        partId: String,
        oldRows: Int,
        addRows: Int,
        projectRows: Int
    ) = repository.updatePartProgress(userId, projectId, partId, oldRows, addRows, projectRows)
}

class DeletePart @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(
        userId: String,
        projectId: String,
        partId: String,
        rows: Int,
        neededRows: Int,
        projectRows: Int,
        projectNeededRows: Int
    ) = repository.deletePart(userId, projectId, partId, rows, neededRows, projectRows, projectNeededRows)
}