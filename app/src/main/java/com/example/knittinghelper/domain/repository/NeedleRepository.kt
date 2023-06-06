package com.example.knittinghelper.domain.repository

import com.example.knittinghelper.domain.model.Needle
import com.example.knittinghelper.util.Response
import kotlinx.coroutines.flow.Flow

interface NeedleRepository {
    fun getUserNeedles(userId: String): Flow<Response<List<Needle>>>
    fun createNeedle(userId: String, type: String, thickness: Float): Flow<Response<Boolean>>
    fun deleteNeedle(userId: String, needleId: String): Flow<Response<Boolean>>
}