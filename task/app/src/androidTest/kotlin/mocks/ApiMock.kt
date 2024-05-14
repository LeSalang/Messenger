package mocks

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder

interface ApiMock {
    val urlPattern: String
    val method: HttpMethod
    val response: ResponseDefinitionBuilder
}

enum class HttpMethod {
    GET,
    POST
}