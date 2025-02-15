package com.lesa.app.data.network

import com.lesa.app.data.network.models.AllMessagesApiDto
import com.lesa.app.data.network.models.AllPresenceApiDto
import com.lesa.app.data.network.models.AllStreamsApiDto
import com.lesa.app.data.network.models.AllSubscribedStreamsApiDto
import com.lesa.app.data.network.models.AllTopicsInStreamApiDto
import com.lesa.app.data.network.models.AllUsersApiDto
import com.lesa.app.data.network.models.MessageResponseApiDto
import com.lesa.app.data.network.models.SendMessageResponseApiDto
import com.lesa.app.data.network.models.UploadFileResponseApiDto
import com.lesa.app.data.network.models.UserApiDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("users")
    suspend fun getAllUsers() : AllUsersApiDto

    @GET("users/me")
    suspend fun getOwnUser() : UserApiDto

    @GET("realm/presence")
    suspend fun getAllUsersPresence(): AllPresenceApiDto

    @GET("streams")
    suspend fun getAllStreams() : AllStreamsApiDto

    @GET("users/me/$SUBSCRIPTIONS")
    suspend fun getAllSubscribedStreams() : AllSubscribedStreamsApiDto

    @GET("users/me/{$STREAM_ID}/topics")
    suspend fun getTopicsInStream(
        @Path(STREAM_ID) id: Int
    ) : AllTopicsInStreamApiDto

    @GET(MESSAGES)
    suspend fun getAllMessagesInStream(
        @Query("num_before") numBefore: Int = 20,
        @Query("num_after") numAfter: Int = 0,
        @Query("include_anchor") includeAnchor: Boolean = false,
        @Query("anchor") anchor: String,
        @Query("narrow") narrow: String
    ) : AllMessagesApiDto

    @POST(MESSAGES)
    suspend fun sendMessage(
        @Query("type") type: String = "stream",
        @Query("to") streamId: Int,
        @Query("topic") topicName: String,
        @Query("content") content: String,
    ) : SendMessageResponseApiDto

    @GET("$MESSAGES/{$MESSAGE_ID}")
    suspend fun getMessage(
        @Path(MESSAGE_ID) messageId: Int
    ) : MessageResponseApiDto

    @POST("$MESSAGES/{$MESSAGE_ID}/reactions")
    suspend fun addReaction(
        @Path(MESSAGE_ID, encoded = true) messageId: Int,
        @Query("emoji_name") emojiName: String
    )

    @DELETE("$MESSAGES/{$MESSAGE_ID}/reactions")
    suspend fun deleteReaction(
        @Path(MESSAGE_ID, encoded = true) messageId: Int,
        @Query("emoji_name") emojiName: String
    )

    @POST(USER_UPLOADS)
    suspend fun uploadFile(
        @Body file: MultipartBody
    ) : UploadFileResponseApiDto

    @POST("users/me/$SUBSCRIPTIONS")
    suspend fun createStream(
        @Query(SUBSCRIPTIONS) subscriptions: String
    )

    @DELETE("$MESSAGES/{$MESSAGE_ID}")
    suspend fun deleteMessage(
        @Path(MESSAGE_ID) messageId: Int
    )

    @PATCH("$MESSAGES/{$MESSAGE_ID}")
    suspend fun editMessageContent(
        @Path(MESSAGE_ID) messageId: Int,
        @Query(CONTENT) content: String,
    )

    @PATCH("$MESSAGES/{$MESSAGE_ID}")
    suspend fun changeMessageTopic(
        @Path(MESSAGE_ID) messageId: Int,
        @Query(TOPIC) topicName: String,
    )

    companion object {
        private const val CONTENT = "content"
        private const val MESSAGES = "messages"
        private const val MESSAGE_ID = "message_id"
        private const val STREAM_ID = "stream_id"
        private const val SUBSCRIPTIONS = "subscriptions"
        private const val TOPIC = "topic"
        private const val USER_ID = "user_id"
        private const val USER_UPLOADS = "user_uploads"
        const val NEWEST_MESSAGE_ANCHOR = "newest"
    }
}

