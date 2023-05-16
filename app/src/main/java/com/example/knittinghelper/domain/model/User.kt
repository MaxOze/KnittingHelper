package com.example.knittinghelper.domain.model

data class User(
    var userId: String = "",
    var userName: String = "",
    var email: String = "",
    var password: String = "",
    var imageUrl: String = "",
    var bio: String = "",
    var following: List<String> = emptyList(),
    var likedPosts: List<String> = emptyList(), // id понравившихся постов
    var yarns: List<Yarn> = emptyList(),
    var needles: List<Needle> = emptyList(),
)
