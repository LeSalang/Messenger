package com.lesa.app.interceptors

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val credential = Credentials.basic("fensalamandra@gmail.com", "qh6tBnbzMVGP8hyuAd0oJV4WGjmUBUjk")
        val requestWithHeader = chain
            .request()
            .newBuilder()
            .addHeader("Authorization", credential)
            .build()
        return chain.proceed(requestWithHeader)
    }
}