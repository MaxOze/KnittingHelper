package com.example.knittinghelper.data

import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.domain.repository.AuthenticationRepository
import com.example.knittinghelper.util.Constants
import com.example.knittinghelper.util.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class AuthenticationRepositoryImpl @Inject constructor(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthenticationRepository {

    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    @ExperimentalCoroutinesApi
    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }

    override fun firebaseSignIn(email: String, password: String): Flow<Response<Boolean>> = flow {
        var operationSuccessful = false
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            emit(Response.Success(operationSuccessful))
        }
        catch(e:Exception){
            emit(Response.Error(e.localizedMessage?:"An Unexpected Error"))
        }
    }

    override fun firebaseSignOut(): Flow<Response<Boolean>> = flow {
        try{
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))
        }
        catch(e:Exception){
            emit(Response.Error(e.localizedMessage?:"An Unexpected Error"))
        }
    }

    override fun firebaseSignUp(
        email: String,
        password: String,
        userName: String,
    ): Flow<Response<Boolean>> = flow{
        var operationSuccessful = false
        try{
            emit(Response.Loading)
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                operationSuccessful = true
            }.await()
            if(operationSuccessful){
                val userId = auth.currentUser?.uid!!
                val obj = User(userName = userName, userId = userId, password = password, email = email)
                firestore.collection(Constants.COLLECTION_NAME_USERS).document(userId)
                    .set(obj).addOnSuccessListener {
                    }
                emit(Response.Success(operationSuccessful))
            }
            else{
                Response.Success(operationSuccessful)
            }
        }
        catch(e:Exception){
            emit(Response.Error(e.localizedMessage?:"An Unexpected Error"))
        }
    }
}


