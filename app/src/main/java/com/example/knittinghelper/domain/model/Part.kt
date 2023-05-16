package com.example.knittinghelper.domain.model

data class Part(
    var projectId: String = "",
    var partId: String = "",
    var name: String = "",
    var text: String = "",
    var needle: String = "",
    var neededRow: Int = 0,
    var countRow: Int = 0,
    var schemeUrls: List<String> = emptyList()
)
