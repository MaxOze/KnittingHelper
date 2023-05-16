package com.example.knittinghelper.domain.model

import com.google.firebase.Timestamp

data class Project(
    var projectId: String = "",
    var userId: String = "",
    var name: String = "",
    var text: String = "",
    var photoUrl: String = "",
    var videoUrl: String = "",
    var needle: String = "",
    var progress: Float = 0.0F,
    var lastUpdate: Timestamp = Timestamp.now()
)
