package com.example.knittinghelper.domain.model

data class User(
    var userId: String = "",
    var userName: String = "",
    var email: String = "",
    var password: String = "",
    var imageUri: String = "",
    var bio: String = "",
    var following: List<String> = emptyList(),
    var likedPosts: List<String> = emptyList(), // id понравившихся постов
)
