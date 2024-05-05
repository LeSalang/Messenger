package com.lesa.app.data.network

import com.lesa.app.data.network.models.AllMessagesApiDto
import com.lesa.app.data.network.models.AllPresenceApiDto
import com.lesa.app.data.network.models.AllStreamsApiDto
import com.lesa.app.data.network.models.AllSubscribedStreamsApiDto
import com.lesa.app.data.network.models.AllTopicsInStreamApiDto
import com.lesa.app.data.network.models.AllUsersApiDto
import com.lesa.app.data.network.models.MessageResponseApiDto
import com.lesa.app.data.network.models.PresenceResponseApiDto
import com.lesa.app.data.network.models.ResponseApiDto
import com.lesa.app.data.network.models.UserApiDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("users")
    suspend fun getAllUsers() : AllUsersApiDto

    @GET("users/me")
    suspend fun getOwnUser() : UserApiDto

    @GET("users/{$USER_ID}/presence")
    suspend fun getUserPresence(@Path(USER_ID) userId: Int): PresenceResponseApiDto

    @GET("realm/presence")
    suspend fun getAllUsersPresence(): AllPresenceApiDto

    @GET("streams")
    suspend fun getAllStreams() : AllStreamsApiDto

    @GET("users/me/subscriptions")
    suspend fun getAllSubscribedStreams() : AllSubscribedStreamsApiDto

    @GET("users/me/{$STREAM_ID}/topics")
    suspend fun getTopicsInStream(
        @Path(STREAM_ID) id: Int
    ) : AllTopicsInStreamApiDto

    @GET("messages")
    suspend fun getAllMessagesInStream(
        @Query("num_before") numBefore: Int = 1000,
        @Query("num_after") numAfter: Int = 1000,
        @Query("anchor") anchor: String = "first_unread",
        @Query("narrow") narrow: String
    ) : AllMessagesApiDto

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String = "stream",
        @Query("to") streamId: Int,
        @Query("topic") topicName: String,
        @Query("content") content: String,
    ) : ResponseApiDto

    @GET("messages/{$MESSAGE_ID}")
    suspend fun getMessage(
        @Path(MESSAGE_ID) messageId: Int
    ) : MessageResponseApiDto

    @POST("messages/{$MESSAGE_ID}/reactions")
    suspend fun addReaction(
        @Path(MESSAGE_ID, encoded = true) messageId: Int,
        @Query("emoji_name") emojiName: String
    )

    @DELETE("messages/{$MESSAGE_ID}/reactions")
    suspend fun deleteReaction(
        @Path(MESSAGE_ID, encoded = true) messageId: Int,
        @Query("emoji_name") emojiName: String
    )

    companion object {
        private const val STREAM_ID = "stream_id"
        private const val MESSAGE_ID = "message_id"
        private const val USER_ID = "user_id"
    }
}

