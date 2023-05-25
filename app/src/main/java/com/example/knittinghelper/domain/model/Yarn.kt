package com.example.knittinghelper.domain.model

data class Yarn(
    val userId: String = "",
    val yarnId: String = "",
    var color: String = "",
    var material: String = "",
    var length: Int = 0,
    var weight: Int = 0,
    var amount: Int = 0,
    var photoUri: String = "",
    var text: String = ""
)
