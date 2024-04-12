package com.lesa.app.api

import com.lesa.app.model.api_models.AllUsersApiDto
import retrofit2.http.GET

interface Api {

    @GET("users")
    suspend fun getAllUsers() : AllUsersApiDto
}

