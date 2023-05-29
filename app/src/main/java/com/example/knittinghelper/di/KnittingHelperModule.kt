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
        firebaseSignUp = FirebaseSignUp(repository = repository),
        deleteUser = DeleteUser(repository = repository)
    )

    @Singleton
    @Provides
    fun providePostRepository(firestore: FirebaseFirestore, storage: FirebaseStorage): PostRepository {
        return PostRepositoryImpl(firestore = firestore, storage = storage)
    }

    @Singleton
    @Provides
    fun providePostUseCases(repository: PostRepository)= PostUseCases(
        getUserPosts = GetUserPosts(repository = repository),
        createPost = CreatePost(repository = repository),
        deletePost = DeletePost(repository = repository),
        getPostsFeed = GetPostsFeed(repository = repository)
    )

    @Singleton
    @Provides
    fun provideUserRepository(firestore: FirebaseFirestore, storage: FirebaseStorage): UserRepository {
        return UserRepositoryImpl(firestore = firestore, storage = storage)
    }

    @Singleton
    @Provides
    fun provideUserUseCases(repository: UserRepository)= UserUseCases(
        getUserDetails = GetUserDetails(repository = repository),
        setUserDetails = SetUserDetails(repository = repository),
        subscribe = Subscribe(repository = repository),
        unSubscribe = UnSubscribe(repository = repository),
        getUserSubscribers = GetUserSubscribers(repository = repository)
    )

    @Singleton
    @Provides
    fun provideProjectRepository(firestore: FirebaseFirestore, storage: FirebaseStorage): ProjectRepository {
        return ProjectRepositoryImpl(firestore = firestore, storage = storage)
    }

    @Singleton
    @Provides
    fun provideProjectUseCases(repository: ProjectRepository)= ProjectUseCases(
        getProject = GetProject(repository = repository),
        getUserProjects = GetUserProjects(repository = repository),
        createProject = CreateProject(repository = repository),
        deleteProject = DeleteProject(repository = repository),
        getPart = GetPart(repository = repository),
        getProjectParts = GetProjectParts(repository = repository),
        createPart = CreatePart(repository = repository),
        updateSimpleProject = UpdateSimpleProject(repository = repository),
        updatePartProgress = UpdatePartProgress(repository = repository),
        deletePart = DeletePart(repository = repository)
    )

    @Singleton
    @Provides
    fun provideNeedleRepository(firestore: FirebaseFirestore): NeedleRepository {
        return NeedleRepositoryImpl(firestore = firestore)
    }

    @Singleton
    @Provides
    fun provideNeedleUseCases(repository: NeedleRepository)= NeedleUseCases(
        getUserNeedles = GetUserNeedles(repository = repository),
        createNeedle = CreateNeedle(repository = repository),
        deleteNeedle = DeleteNeedle(repository = repository)
    )

    @Singleton
    @Provides
    fun provideYarnRepository(firestore: FirebaseFirestore, storage: FirebaseStorage): YarnRepository {
        return YarnRepositoryImpl(firestore = firestore, storage = storage)
    }

    @Singleton
    @Provides
    fun provideYarnUseCases(repository: YarnRepository)= YarnUseCases(
        getUserYarns = GetUserYarns(repository = repository),
        createYarn = CreateYarn(repository = repository),
        updateYarn = UpdateYarn(repository = repository),
        deleteYarn = DeleteYarn(repository = repository)
    )
}