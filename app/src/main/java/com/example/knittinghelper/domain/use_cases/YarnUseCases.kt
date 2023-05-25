package com.example.knittinghelper.domain.use_cases

import android.net.Uri
import com.example.knittinghelper.domain.repository.YarnRepository
import javax.inject.Inject

data class YarnUseCases(
    val getUserYarns: GetUserYarns,
    val createYarn: CreateYarn,
    val updateYarn: UpdateYarn,
    val deleteYarn: DeleteYarn
)

class GetUserYarns @Inject constructor(
    private val repository: YarnRepository
) {
    operator fun invoke(userId: String) = repository.getUserYarns(userId)
}

class CreateYarn @Inject constructor(
    private val repository: YarnRepository
) {
    operator fun invoke(
        userId: String,
        color: String,
        material: String,
        length: Int,
        weight: Int,
        amount: Int,
        photoUri: Uri?
    ) = repository.createYarn(userId, color, material, length, weight, amount, photoUri)
}

class UpdateYarn @Inject constructor(
    private val repository: YarnRepository
) {
    operator fun invoke(
        yarnId: String,
        text: String,
        count: Int
    ) = repository.updateYarn(yarnId, text, count)
}

class DeleteYarn @Inject constructor(
    private val repository: YarnRepository
) {
    operator fun invoke(yarnId: String) = repository.deleteYarn(yarnId)
}