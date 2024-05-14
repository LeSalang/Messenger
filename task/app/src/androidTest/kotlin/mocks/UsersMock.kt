package mocks

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import utils.AssetsUtils

sealed class UsersMock : ApiMock {
    data object GetOwnUser: UsersMock()
    data object GetAllUsers: UsersMock()
    data object GetAllUsersPresence: UsersMock()

    override val method: HttpMethod
        get() = when (this) {
            GetAllUsers -> HttpMethod.GET
            GetAllUsersPresence -> HttpMethod.GET
            GetOwnUser -> HttpMethod.GET
        }

    override val urlPattern: String
        get() = when (this) {
            GetOwnUser -> "/users/me$"
            GetAllUsers -> "/users$"
            GetAllUsersPresence -> "/realm/presence$"
        }

    override val response: ResponseDefinitionBuilder
        get() = when (this) {
            GetOwnUser -> WireMock.ok(AssetsUtils.fromAssets("users/getOwnUser.json"))
            GetAllUsers -> WireMock.ok(AssetsUtils.fromAssets("users/getAllUsers.json"))
            GetAllUsersPresence -> WireMock.ok(AssetsUtils.fromAssets("users/getAllUsersPresence.json"))
        }
}