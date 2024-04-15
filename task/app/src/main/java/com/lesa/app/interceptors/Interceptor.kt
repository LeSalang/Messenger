package com.lesa.app.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val credential = "Basic ZmVuc2FsYW1hbmRyYUBnbWFpbC5jb206cWg2dEJuYnpNVkdQOGh5dUFkMG9KVjRXR2ptVUJVams="
        val requestWithHeader = chain
            .request()
            .newBuilder()
            .addHeader("Authorization", credential)
            .build()
        return chain.proceed(requestWithHeader)
    }
}