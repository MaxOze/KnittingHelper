package com.example.knittinghelper.domain.model

data class User(
    var userName : String = "",
    var userId : String = "",
    var email : String = "",
    var password : String = "",
    var imageUrl : String = "",
    var bio : String = "",
    var following : List<String> = emptyList(),
    var followers : List<String> = emptyList()
)
