package mocks

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import utils.AssetsUtils

sealed class MessagesMock : ApiMock {
    data object GetAllMessagesInStream: MessagesMock()
    data object GetMessage: MessagesMock()
    data object SendMessage: MessagesMock()

    override val method: HttpMethod
        get() = when (this) {
            GetAllMessagesInStream -> HttpMethod.GET
            SendMessage -> HttpMethod.POST
            GetMessage -> HttpMethod.GET
        }

    override val urlPattern: String
        get() = when (this) {
            GetAllMessagesInStream -> "/messages?"
            SendMessage -> "/messages"
            GetMessage -> "/messages/438647247"
        }

    override val response: ResponseDefinitionBuilder
        get() = when (this) {
            GetAllMessagesInStream -> WireMock.ok(AssetsUtils.fromAssets("messages/getAllMessagesInStream.json"))
            SendMessage -> WireMock.ok(AssetsUtils.fromAssets("messages/sendMessage.json"))
            GetMessage -> WireMock.ok(AssetsUtils.fromAssets("messages/getMessage.json"))
        }
}