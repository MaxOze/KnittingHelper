package com.example.knittinghelper.domain.repository

import android.net.Uri
import com.example.knittinghelper.domain.model.Yarn
import com.example.knittinghelper.util.Response
import kotlinx.coroutines.flow.Flow

interface YarnRepository {
    fun getUserYarns(userId: String): Flow<Response<List<Yarn>>>
    fun createYarn(
        userId: String,
        color: String,
        material: String,
        length: Int,
        weight: Int,
        amount: Int,
        photoUri: Uri?
    ): Flow<Response<Boolean>>
    fun updateYarn(
        userId: String,
        yarnId: String,
        text: String,
        count: Int
    ): Flow<Response<Boolean>>
    fun deleteYarn(userId: String, yarnId: String): Flow<Response<Boolean>>
}