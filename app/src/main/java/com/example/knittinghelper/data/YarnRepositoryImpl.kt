package com.example.knittinghelper.data

import android.net.Uri
import com.example.knittinghelper.domain.model.Yarn
import com.example.knittinghelper.domain.repository.YarnRepository
import com.example.knittinghelper.util.Constants
import com.example.knittinghelper.util.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class YarnRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : YarnRepository {
    override fun getUserYarns(userId: String): Flow<Response<List<Yarn>>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_YARNS)
            .whereEqualTo("userId", userId)
            .orderBy("amount", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot!=null) {
                    val yarns = snapshot.toObjects(Yarn::class.java)
                    Response.Success(yarns)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun createYarn(
        userId: String,
        color: String,
        material: String,
        length: Int,
        weight: Int,
        amount: Int,
        photoUri: Uri?
    ): Flow<Response<Boolean>> = flow {
        var operationSuccessful = false
        try {
            val yarnId = firestore.collection(Constants.COLLECTION_NAME_YARNS).document().id
            val newYarn = Yarn(
                userId = userId,
                yarnId = yarnId,
                color = color,
                material = material,
                length = length,
                weight = weight,
                amount = amount
            )
            if (photoUri != null) {
                val uri = storage.reference.child(Constants.FOLDER_NAME_YARNS)
                    .child(yarnId)
                    .putFile(photoUri).await()
                    .storage.downloadUrl.await()
                newYarn.photoUri = uri.toString()
            }
            firestore.collection(Constants.COLLECTION_NAME_YARNS)
                .document(yarnId).set(newYarn)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось добавить пряжу!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }

    override fun updateYarn(yarnId: String, text: String, count: Int): Flow<Response<Boolean>> = flow {
        var operationSuccessful = false
        try {
            if (count != -1) {
                firestore.collection(Constants.COLLECTION_NAME_YARNS)
                    .document(yarnId).update("amount", count).addOnSuccessListener {
                        operationSuccessful = true
                    }.await()
            } else {
                firestore.collection(Constants.COLLECTION_NAME_YARNS)
                    .document(yarnId).update("text", text).addOnSuccessListener {
                        operationSuccessful = true
                    }.await()
            }
            if (operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Изменения не применены!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }

    override fun deleteYarn(yarnId: String): Flow<Response<Boolean>> = flow {
        var operationSuccessful = false
        try {
            firestore.collection(Constants.COLLECTION_NAME_YARNS)
                .document(yarnId).delete().addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if (operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось удалить пряжу!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }
}