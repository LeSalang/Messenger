package com.lesa.app

import android.content.Context
import com.lesa.app.api.Api
import com.lesa.app.interceptors.AuthHeaderInterceptor
import com.lesa.app.repositories.StreamsRepository
import com.lesa.app.repositories.StreamsRepositoryImpl
import com.lesa.app.repositories.UserRepository
import com.lesa.app.repositories.UserRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val userRepository: UserRepository
    val streamsRepository: StreamsRepository
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

    val retrofit = Retrofit.Builder()
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
}