package com.example.knittinghelper.domain.model

data class Post(
    var postId: String = "",
    var userId: String = "",
    var userName: String = "",
    var photoUrls: List<String> = emptyList(),
    var text: String = "",
    var postDate: String = ""
)
