package com.example.knittinghelper.domain.model

import com.google.firebase.Timestamp

data class Part(
    var projectId: String = "",
    var partId: String = "",
    var name: String = "",
    var text: String = "",
    var needle: String = "",
    var neededRow: Int = 0,
    var countRow: Int = 0,
    var photoUri: String = "",
    var schemeUrls: List<String> = emptyList(),
    var lastUpdate: Timestamp = Timestamp.now()
)
