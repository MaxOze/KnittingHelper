package com.example.knittinghelper.data

import com.example.knittinghelper.domain.model.Needle
import com.example.knittinghelper.domain.repository.NeedleRepository
import com.example.knittinghelper.util.Constants
import com.example.knittinghelper.util.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NeedleRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NeedleRepository {

    override fun getUserNeedles(userId: String): Flow<Response<List<Needle>>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_NEEDLES)
            .whereEqualTo("userId", userId)
            .orderBy("type")
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot!=null) {
                    val needles = snapshot.toObjects(Needle::class.java)
                    Response.Success(needles)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun createNeedle(
        userId: String,
        type: String,
        thickness: Float
    ): Flow<Response<Boolean>> = flow {
        var operationSuccessful = false
        try {
            val needleId = firestore.collection(Constants.COLLECTION_NAME_NEEDLES).document().id
            val newNeedle = Needle(
                userId = userId,
                needleId = needleId,
                type = type,
                thickness = thickness
            )
            firestore.collection(Constants.COLLECTION_NAME_NEEDLES)
                .document(needleId).set(newNeedle)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось добавить инструмент!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }

    override fun deleteNeedle(needleId: String): Flow<Response<Boolean>> = flow{
        var operationSuccessful = false
        try {
            firestore.collection(Constants.COLLECTION_NAME_NEEDLES)
                .document(needleId).delete()
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось удалить инструмент!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }
}