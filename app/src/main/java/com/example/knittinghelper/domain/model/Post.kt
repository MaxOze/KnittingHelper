package com.example.knittinghelper.domain.model

import com.google.firebase.Timestamp

data class Post(
    var userId: String = "",
    var postId: String = "",
    var userName: String = "",
    var userPhotoUri: String = "",
    var photoUris: List<String> = emptyList(),
    var text: String = "",
    var postDate: Timestamp = Timestamp.now(),
    var needle: String = ""
)
