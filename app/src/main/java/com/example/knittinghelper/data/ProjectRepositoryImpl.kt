package com.example.knittinghelper.data


import com.example.knittinghelper.domain.model.Part
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.domain.repository.ProjectRepository
import com.example.knittinghelper.util.Constants
import com.example.knittinghelper.util.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProjectRepository {
    private var operationSuccessful = false

    override fun getProject(projectId: String): Flow<Response<Project>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
            .document(projectId)
            .addSnapshotListener{ snapshot, error->
                val response = if(snapshot!=null) {
                    val projectInfo = snapshot.toObject(Project::class.java)
                    Response.Success<Project>(projectInfo!!)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun getUserProjects(userId: String): Flow<Response<List<Project>>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
            .whereEqualTo("userId", userId)
            .orderBy("lastUpdate")
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot!=null) {
                    val projects = snapshot.toObjects(Project::class.java)
                    Response.Success<List<Project>>(projects)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun createProject(
        userId: String,
        name: String,
        text: String,
        photoUrl: String,
        videoUrl: String,
        needle: String
    ): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            val projectId = firestore.collection(Constants.COLLECTION_NAME_PROJECTS).document().id
            val newProject = Project(
                projectId = projectId,
                userId = userId,
                name = name,
                text = text,
                photoUrl = photoUrl,
                videoUrl = videoUrl,
                needle = needle
            )
            firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
                .document(projectId).set(newProject)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось создать проект!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }

    override fun getPart(projectId: String, partId: String): Flow<Response<Part>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_PROJECTS).document(projectId)
            .collection(Constants.COLLECTION_NAME_PARTS).document(partId)
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot!=null) {
                    val partInfo = snapshot.toObject(Part::class.java)
                    Response.Success<Part>(partInfo!!)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun getProjectParts(projectId: String): Flow<Response<List<Part>>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_PROJECTS).document(projectId)
            .collection(Constants.COLLECTION_NAME_PARTS)
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot!=null) {
                    val parts = snapshot.toObjects(Part::class.java)
                    Response.Success<List<Part>>(parts)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun createPart(
        projectId: String,
        name: String,
        text: String,
        needle: String,
        schemeUrls: List<String>,
        neededRow: Int
    ): Flow<Response<Boolean>> = flow{
        operationSuccessful = false
        try {
            val partId = firestore.collection(Constants.COLLECTION_NAME_PROJECTS).document(projectId)
                .collection(Constants.COLLECTION_NAME_PARTS).document().id
            val newPart = Part(
                projectId = projectId,
                name = name,
                text = text,
                needle = needle,
                neededRow = neededRow,
                schemeUrls = schemeUrls
            )
            firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
                .document(projectId).collection(Constants.COLLECTION_NAME_PARTS)
                .document(partId).set(newPart)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось создать часть!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }
}