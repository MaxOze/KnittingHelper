package com.example.knittinghelper.data

import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.domain.repository.UserRepository
import com.example.knittinghelper.util.Constants
import com.example.knittinghelper.util.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    private var operationSuccessful = false

    override fun getUserDetails(userId: String): Flow<Response<User>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_USERS)
            .document(userId)
            .addSnapshotListener{ snapshot, error->
                val response = if(snapshot!=null) {
                    val userInfo = snapshot.toObject(User::class.java)
                    Response.Success<User>(userInfo!!)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun setUserDetails(userId: String, userName: String, bio: String): Flow<Response<Boolean>> = flow {
        operationSuccessful = false
        try {
            val userObj = mutableMapOf<String,String>()
            userObj["userName"] = userName
            userObj["bio"] = bio
            firestore.collection(Constants.COLLECTION_NAME_USERS)
                .document(userId)
                .update(userObj as Map<String, Any>)
                .addOnSuccessListener {  }.await()
            if(operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Edit Does Not Succed"))
            }
        } catch(e:Exception) {
            Response.Error(e.localizedMessage?:"An Unexpected Error")
        }
    }
}