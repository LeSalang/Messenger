package com.lesa.app

import com.lesa.app.data.network.Api
import com.lesa.app.data.network.interceptors.AuthHeaderInterceptor
import com.lesa.app.data.repositories.MessagesRepository
import com.lesa.app.data.repositories.MessagesRepositoryImpl
import com.lesa.app.data.repositories.StreamsRepository
import com.lesa.app.data.repositories.StreamsRepositoryImpl
import com.lesa.app.data.repositories.UserRepository
import com.lesa.app.data.repositories.UserRepositoryImpl
import com.lesa.app.domain.use_cases.chat.AddReactionUseCase
import com.lesa.app.domain.use_cases.chat.DeleteReactionUseCase
import com.lesa.app.domain.use_cases.chat.LoadAllMessagesUseCase
import com.lesa.app.domain.use_cases.chat.LoadSelectedMessageUseCase
import com.lesa.app.domain.use_cases.chat.SendMessageUseCase
import com.lesa.app.domain.use_cases.people.LoadPeopleUseCase
import com.lesa.app.domain.use_cases.profile.LoadProfileUseCase
import com.lesa.app.domain.use_cases.streams.LoadStreamsUseCase
import com.lesa.app.presentation.features.chat.elm.ChatActor
import com.lesa.app.presentation.features.chat.elm.ChatStoreFactory
import com.lesa.app.presentation.features.people.elm.PeopleActor
import com.lesa.app.presentation.features.people.elm.PeopleStoreFactory
import com.lesa.app.presentation.features.profile.elm.ProfileActor
import com.lesa.app.presentation.features.profile.elm.ProfileStoreFactory
import com.lesa.app.presentation.features.streams.elm.StreamsActor
import com.lesa.app.presentation.features.streams.elm.StreamsStoreFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val profileStoreFactory: ProfileStoreFactory
    val peopleStoreFactory: PeopleStoreFactory
    val streamsStoreFactory: StreamsStoreFactory
    val chatStoreFactory: ChatStoreFactory
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

    override val profileStoreFactory by lazyNone {
        ProfileStoreFactory(profileActor)
    }

    private val profileActor by lazyNone {
        ProfileActor(loadProfileUseCase)
    }

    private val loadProfileUseCase by lazyNone {
        LoadProfileUseCase(userRepository)
    }

    override val peopleStoreFactory by lazyNone {
        PeopleStoreFactory(peopleActor)
    }

    private val peopleActor by lazyNone {
        PeopleActor(loadPeopleUseCase)
    }

    private val loadPeopleUseCase by lazyNone {
        LoadPeopleUseCase(userRepository)
    }

    override val streamsStoreFactory by lazyNone {
        StreamsStoreFactory(streamsActor)
    }

    private val streamsActor by lazyNone {
        StreamsActor(loadStreamsUseCase)
    }

    private val loadStreamsUseCase by lazyNone {
        LoadStreamsUseCase(streamsRepository)
    }

    override val chatStoreFactory by lazyNone {
        ChatStoreFactory(chatActor)
    }

    private val chatActor by lazyNone {
        ChatActor(
            loadAllMessagesUseCase = loadAllMessagesUseCase,
            sendMessageUseCase = sendMessageUseCase,
            loadSelectedMessageUseCase = loadSelectedMessageUseCase,
            addReactionUseCase = addReactionUseCase,
            deleteReactionUseCase = deleteReactionUseCase
        )
    }

    private val loadAllMessagesUseCase by lazyNone {
        LoadAllMessagesUseCase(messagesRepository)
    }

    private val sendMessageUseCase by lazyNone {
        SendMessageUseCase(messagesRepository)
    }

    private val loadSelectedMessageUseCase by lazyNone {
        LoadSelectedMessageUseCase(messagesRepository)
    }

    private val addReactionUseCase by lazyNone {
        AddReactionUseCase(messagesRepository)
    }

    private val deleteReactionUseCase by lazyNone {
        DeleteReactionUseCase(messagesRepository)
    }
}

private fun <T> lazyNone(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)