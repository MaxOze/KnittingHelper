package com.example.knittinghelper.data


import android.net.Uri
import com.example.knittinghelper.domain.model.Part
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.domain.repository.ProjectRepository
import com.example.knittinghelper.util.Constants
import com.example.knittinghelper.util.Response
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ProjectRepository {
    private var operationSuccessful = false

    override fun getProject(projectId: String): Flow<Response<Project>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
            .document(projectId)
            .addSnapshotListener{ snapshot, error->
                val response = if(snapshot!=null) {
                    val projectInfo = snapshot.toObject(Project::class.java)
                    Response.Success(projectInfo!!)
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
            .orderBy("lastUpdate", Query.Direction.DESCENDING)
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
        photoUri: Uri?,
        videoUri: String,
        needle: String,
        simpleProject: Boolean,
        neededRow: Int
    ): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            val projectId = firestore.collection(Constants.COLLECTION_NAME_PROJECTS).document().id
            val newProject = Project(
                userId = userId,
                projectId = projectId,
                name = name,
                text = text,
                videoUri = videoUri,
                needle = needle,
                simpleProject = simpleProject
            )
            if(photoUri !== null) {
                val uri = storage.reference.child(Constants.FOLDER_NAME_PROJECTS)
                    .child(projectId)
                    .putFile(photoUri).await()
                    .storage.downloadUrl.await()
                newProject.photoUri = uri.toString()
            }

            if (simpleProject) newProject.neededRows = neededRow

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

    override fun deleteProject(projectId: String): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        var operationSuccessful2 = false
        try {
            firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
                .document(projectId).delete()
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            firestore.collection(Constants.COLLECTION_NAME_PARTS)
                .whereEqualTo("projectId", projectId).get()
                .addOnSuccessListener {
                    for (document in it) {
                        document.reference.delete();
                    }
                    operationSuccessful2 = true
                }.await()
            if(operationSuccessful && operationSuccessful2) {
                emit(Response.Success(operationSuccessful))
            } else if (!operationSuccessful) {
                emit(Response.Error("Не удалось удалить проект!"))
            } else if (!operationSuccessful2) {
                emit(Response.Error("Не удалось удалить части проекта!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }

    override fun getPart(partId: String): Flow<Response<Part>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_PARTS)
            .document(partId)
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot!=null) {
                    val partInfo = snapshot.toObject(Part::class.java)
                    Response.Success(partInfo!!)
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
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_PARTS)
            .whereEqualTo("projectId", projectId)
            .orderBy("lastUpdate", Query.Direction.DESCENDING)
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
        photoUri: Uri?,
        neededRow: Int,
        projectNeededRows: Int
    ): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            val partId = firestore.collection(Constants.COLLECTION_NAME_PARTS).document().id
            val newPart = Part(
                projectId = projectId,
                partId = partId,
                name = name,
                text = text,
                needle = needle,
                neededRow = neededRow
            )
            if (photoUri !== null) {
                val uri = storage.reference.child(Constants.FOLDER_NAME_PARTS)
                    .child(partId)
                    .putFile(photoUri).await()
                    .storage.downloadUrl.await()
                newPart.photoUri = uri.toString()
            }

            firestore.collection(Constants.COLLECTION_NAME_PARTS)
                .document(partId).set(newPart)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful) {
                firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
                    .document(projectId).update("neededRows", projectNeededRows + neededRow)
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось создать часть!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }

    override fun updateSimpleProject(projectId: String, newRows: Int): Flow<Response<Boolean>> = flow {
        firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
            .document(projectId).update("countRows", newRows)
    }

    override fun updatePartProgress(
        projectId: String,
        partId: String,
        oldRows: Int,
        addRows: Int,
        projectRows: Int
    ): Flow<Response<Boolean>> = flow {
        firestore.collection(Constants.COLLECTION_NAME_PARTS)
            .document(partId).update("countRow",addRows)
        firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
            .document(projectId).update("countRows", projectRows + (addRows - oldRows),
                                        "lastUpdate", Timestamp.now())
    }

    override fun deletePart(
        projectId: String,
        partId: String,
        rows: Int,
        neededRows: Int,
        projectRows: Int,
        projectNeededRows: Int): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            firestore.collection(Constants.COLLECTION_NAME_PARTS)
                .document(partId).delete()
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful) {
                firestore.collection(Constants.COLLECTION_NAME_PROJECTS)
                    .document(projectId).update(
                        "countRows", projectRows - rows,
                        "neededRows", projectNeededRows - neededRows
                    )
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось удалить часть!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }
}