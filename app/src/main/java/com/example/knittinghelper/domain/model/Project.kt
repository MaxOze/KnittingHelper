package com.example.knittinghelper.domain.model

import com.google.firebase.Timestamp

data class Project(
    var projectId: String = "",
    var name: String = "",
    var text: String = "",
    var photoUri: String = "",
    var videoUri: String = "",
    var needle: String = "",
    var countRows: Int = 0,
    var neededRows: Int = 0,
    var lastUpdate: Timestamp = Timestamp.now(),
)
