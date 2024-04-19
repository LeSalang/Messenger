package com.lesa.app

import com.lesa.app.data.network.Api
import com.lesa.app.data.network.interceptors.AuthHeaderInterceptor
import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.data.repositories.MessagesRepositoryImpl
import com.lesa.app.data.repositories.StreamsRepository
import com.lesa.app.data.repositories.StreamsRepositoryImpl
import com.lesa.app.data.repositories.UserRepository
import com.lesa.app.data.repositories.UserRepositoryImpl
import com.lesa.app.domain.use_cases.profile.LoadProfileUseCase
import com.lesa.app.presentation.features.profile.ProfileActor
import com.lesa.app.presentation.features.profile.ProfileStoreFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val storeFactory: ProfileStoreFactory
    val userRepository: UserRepository
    val streamsRepository: StreamsRepository
    val messagesRepository: MessagesRepository
}

class DefaultAppContainer() : AppContainer {
    private val baseUrl = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"

    private val authClient = OkHttpClient
        .Builder()
        .addInterceptor(AuthHeaderInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            jsonSerializer.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()
            )
        )
        .client(authClient)
        .build()

    private val api: Api by lazy {
        retrofit.create(Api::class.java)
    }

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(api = api)
    }

    override val streamsRepository: StreamsRepository by lazy {
        StreamsRepositoryImpl(api = api)
    }

    override val messagesRepository: MessagesRepository by lazy {
        MessagesRepositoryImpl(api = api)
    }

    override val storeFactory by lazyNone {
        ProfileStoreFactory(profileActor)
    }

    private val profileActor by lazyNone {
        ProfileActor(loadProfileUseCase)
    }

    private val loadProfileUseCase by lazyNone {
        LoadProfileUseCase(userRepository)
    }
}

private fun <T> lazyNone(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)