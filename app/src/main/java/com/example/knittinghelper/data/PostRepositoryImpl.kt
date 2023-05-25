package com.example.knittinghelper.data

import android.net.Uri
import com.example.knittinghelper.domain.model.Post
import com.example.knittinghelper.domain.model.Yarn
import com.example.knittinghelper.domain.repository.PostRepository
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

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : PostRepository {
    override fun getUserPosts(userId: String): Flow<Response<List<Post>>> = callbackFlow {
        Response.Loading
        val snapShotListener = firestore.collection(Constants.COLLECTION_NAME_POSTS)
            .whereEqualTo("userId", userId)
            .orderBy("postDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                val response = if(snapshot!=null) {
                    val posts = snapshot.toObjects(Post::class.java)
                    Response.Success(posts)
                } else {
                    Response.Error(error?.message?:error.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapShotListener.remove()
        }
    }

    override fun createPost(
        userId: String,
        userName: String,
        userPhotoUri: String,
        photoUris: List<Uri>?,
        text: String,
        needle: String
    ): Flow<Response<Boolean>> = flow {
        var operationSuccessful = false
        try {
            val postId = firestore.collection(Constants.COLLECTION_NAME_POSTS).document().id
            val newPost = Post(
                userId = userId,
                postId = postId,
                userPhotoUri = userPhotoUri,
                userName = userName,
                text = text,
                needle = needle

            )
            if (photoUris != null) {
                val uriList : ArrayList<String> = arrayListOf()
                photoUris.forEachIndexed() { index, URI ->
                    val uri = storage.reference.child(Constants.FOLDER_NAME_POSTS)
                        .child(postId)
                        .child(index.toString())
                        .putFile(URI).await()
                        .storage.downloadUrl.await()
                    uriList.add(uri.toString())
                }
                newPost.photoUris = uriList
            }
            firestore.collection(Constants.COLLECTION_NAME_POSTS)
                .document(postId).set(newPost)
                .addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if(operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось добавить пост!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }

    override fun deletePost(postId: String): Flow<Response<Boolean>> = flow {
        var operationSuccessful = false
        try {
            firestore.collection(Constants.COLLECTION_NAME_POSTS)
                .document(postId).delete().addOnSuccessListener {
                    operationSuccessful = true
                }.await()
            if (operationSuccessful) {
                emit(Response.Success(operationSuccessful))
            } else {
                emit(Response.Error("Не удалось удалить пост!"))
            }
        } catch(e:Exception) {
            emit(Response.Error(e.localizedMessage?:"Неизвестная ошибка"))
        }
    }

    override fun getPostsFeed(userId: String, count: Int): Flow<Response<List<Post>>> {
        TODO("Not yet implemented")
    }
}