package com.example.knittinghelper.domain.use_cases

import com.example.knittinghelper.domain.repository.ProjectRepository
import javax.inject.Inject

data class ProjectUseCases(
    val getProject: GetProject,
    val getUserProjects: GetUserProjects,
    val createProject: CreateProject,
    val getPart: GetPart,
    val getProjectParts: GetProjectParts,
    val createPart: CreatePart
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
        photoUrl: String,
        videoUrl: String,
        needle: String
    ) = repository.createProject(userId, name, text, photoUrl, videoUrl, needle)
}

class GetPart @Inject constructor(
    private val repository: ProjectRepository
) {
    operator fun invoke(projectId: String,partId: String) = repository.getPart(projectId, partId)
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
        schemeUrls: List<String>,
        neededRow: Int
    ) = repository.createPart(projectId, name, text, needle, schemeUrls, neededRow)
}