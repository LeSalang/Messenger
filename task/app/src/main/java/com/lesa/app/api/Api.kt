package com.lesa.app.api

import com.lesa.app.model.api_models.AllStreamsApiDto
import com.lesa.app.model.api_models.AllSubscribedStreamsApiDto
import com.lesa.app.model.api_models.AllTopicsInStreamApiDto
import com.lesa.app.model.api_models.AllUsersApiDto
import com.lesa.app.model.api_models.UserApiDto
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("users")
    suspend fun getAllUsers() : AllUsersApiDto

    @GET("users/me")
    suspend fun getOwnUser() : UserApiDto

    @GET("streams")
    suspend fun getAllStreams() : AllStreamsApiDto

    @GET("users/me/subscriptions")
    suspend fun getAllSubscribedStreams() : AllSubscribedStreamsApiDto

    @GET("users/me/{stream_id}/topics")
    suspend fun getTopicsInStream(
        @Path("stream_id") id: Int
    ) : AllTopicsInStreamApiDto
}

