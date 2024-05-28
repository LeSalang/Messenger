package com.lesa.androidTest.mocks

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.github.tomakehurst.wiremock.matching.UrlPattern

class ApiMockServer(private val wireMockServer: WireMockServer) {
    fun mock(mock: ApiMock) {
        val urlPattern = urlPathMatching(mock.urlPattern)
        val matcher = matcher(method = mock.method, urlPattern = urlPattern)
        wireMockServer.stubFor(matcher.willReturn(mock.response))
    }

    private fun matcher(method: HttpMethod, urlPattern: UrlPattern): MappingBuilder {
        return when (method) {
            HttpMethod.GET -> WireMock.get(urlPattern)
            HttpMethod.POST -> WireMock.post(urlPattern)
        }
    }

    companion object {
        fun WireMockServer.api(block: ApiMockServer.() -> Unit) {
            ApiMockServer(this).apply(block)
        }
    }
}