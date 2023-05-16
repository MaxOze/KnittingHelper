package com.example.knittinghelper.di

import com.example.knittinghelper.data.*
import com.example.knittinghelper.domain.repository.*
import com.example.knittinghelper.domain.use_cases.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KnittingHelperModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthentication():FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore():FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage():FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthenticationRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthenticationRepository {
        return AuthenticationRepositoryImpl(auth = auth, firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideAuthUseCases(repository: AuthenticationRepository)= AuthenticationUseCases(
        isUserAuthenticated = IsUserAuthenticated(repository = repository),
        firebaseAuthState = FirebaseAuthState(repository = repository),
        firebaseSignOut = FirebaseSignOut(repository = repository),
        firebaseSignIn = FirebaseSignIn(repository = repository),
        firebaseSignUp = FirebaseSignUp(repository = repository)
    )

    @Singleton
    @Provides
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideUserUseCases(repository: UserRepository)= UserUseCases(
        getUserDetails = GetUserDetails(repository = repository),
        setUserDetails = SetUserDetails(repository = repository)
    )

    @Singleton
    @Provides
    fun provideProjectRepository(firestore: FirebaseFirestore): ProjectRepository {
        return ProjectRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideProjectUseCases(repository: ProjectRepository)= ProjectUseCases(
        getProject = GetProject(repository = repository),
        getUserProjects = GetUserProjects(repository = repository),
        createProject = CreateProject(repository = repository),
        getPart = GetPart(repository = repository),
        getProjectParts = GetProjectParts(repository = repository),
        createPart = CreatePart(repository = repository)
    )
}