package com.example.knittinghelper.domain.use_cases

import com.example.knittinghelper.domain.repository.NeedleRepository
import javax.inject.Inject

data class NeedleUseCases(
    val getUserNeedles: GetUserNeedles,
    val createNeedle: CreateNeedle,
    val deleteNeedle: DeleteNeedle
)

class GetUserNeedles @Inject constructor(
    private val repository: NeedleRepository
) {
    operator fun invoke(userId: String) = repository.getUserNeedles(userId)
}

class CreateNeedle @Inject constructor(
    private val repository: NeedleRepository
) {
    operator fun invoke(
        userId: String,
        type: String,
        thickness: Float
    ) = repository.createNeedle(userId, type, thickness)
}

class DeleteNeedle @Inject constructor(
    private val repository: NeedleRepository
) {
    operator fun invoke(needleId: String) = repository.deleteNeedle(needleId)
}

