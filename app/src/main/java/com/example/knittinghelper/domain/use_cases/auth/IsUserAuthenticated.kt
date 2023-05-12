package com.example.knittinghelper.domain.use_cases.auth

import com.example.knittinghelper.domain.repository.AuthenticationRepository
import javax.inject.Inject

class IsUserAuthenticated @Inject constructor(
    private val repository: AuthenticationRepository
){
    operator fun invoke() = repository.isUserAuthenticatedInFirebase()
}