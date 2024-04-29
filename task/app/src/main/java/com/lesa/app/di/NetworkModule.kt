package com.lesa.app.di

import com.lesa.app.data.network.Api
import com.lesa.app.data.network.interceptors.AuthHeaderInterceptor
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideApi() : Api {
        val authClient = OkHttpClient
            .Builder()
            .addInterceptor(AuthHeaderInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

        val jsonSerializer = Json {
            ignoreUnknownKeys = true
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                jsonSerializer.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .client(authClient)
            .build()

        return retrofit.create(Api::class.java)
    }

    companion object {
        private const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
    }
}