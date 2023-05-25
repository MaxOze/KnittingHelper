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
    operator fun invoke(projectId: String) = repository.getProject(projectId)
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
        simpleProject: Boolean,
        neededRow: Int
    ) = repository.createProject(userId, name, text, photoUri, videoUri, needle, simpleProject, neededRow)
}

class DeleteProject @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(projectId: String) = repository.deleteProject(projectId)
}

class GetPart @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(partId: String) = repository.getPart(partId)
}


class GetProjectParts @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(projectId: String) = repository.getProjectParts(projectId)
}

class CreatePart @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(
        projectId: String,
        name: String,
        text: String,
        needle: String,
        photoUri: Uri?,
        neededRow: Int,
        projectNeededRows: Int
    ) = repository.createPart(projectId, name, text, needle, photoUri, neededRow, projectNeededRows)
}

class UpdatePartProgress @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(
        projectId: String,
        partId: String,
        oldRows: Int,
        addRows: Int,
        projectRows: Int
    ) = repository.updatePartProgress(projectId, partId, oldRows, addRows, projectRows)
}

class DeletePart @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(
        projectId: String,
        partId: String,
        rows: Int,
        neededRows: Int,
        projectRows: Int,
        projectNeededRows: Int
    ) = repository.deletePart(projectId, partId, rows, neededRows, projectRows, projectNeededRows)
}