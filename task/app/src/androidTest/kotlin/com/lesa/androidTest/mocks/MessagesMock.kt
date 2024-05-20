package com.lesa.androidTest.mocks

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.lesa.androidTest.utils.AssetsUtils

sealed class MessagesMock : ApiMock {
    data object GetAllMessagesInStream: MessagesMock()
    data class GetMessage(val messageId: String = "438647247"): MessagesMock()
    data object SendMessage: MessagesMock()

    override val method: HttpMethod
        get() = when (this) {
            GetAllMessagesInStream -> HttpMethod.GET
            is GetMessage -> HttpMethod.GET
            SendMessage -> HttpMethod.POST
        }

    override val urlPattern: String
        get() = when (this) {
            GetAllMessagesInStream -> "/messages"
            is GetMessage -> "/messages/$messageId"
            SendMessage -> "/messages"
        }

    override val response: ResponseDefinitionBuilder
        get() = when (this) {
            GetAllMessagesInStream -> WireMock.ok(AssetsUtils.string("messages/getAllMessagesInStream.json"))
            is GetMessage -> WireMock.ok(AssetsUtils.string("messages/getMessage.json"))
            SendMessage -> WireMock.ok(AssetsUtils.string("messages/sendMessage.json"))
        }
}