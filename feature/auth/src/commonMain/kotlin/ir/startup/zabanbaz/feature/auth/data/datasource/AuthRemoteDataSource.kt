package ir.startup.zabanbaz.feature.auth.data.datasource

import ir.startup.zabanbaz.core.networking.ApiService
import ir.startup.zabanbaz.core.networking.HttpClientFactory
import ir.startup.zabanbaz.feature.auth.data.dto.RequestCodeRequestDto
import ir.startup.zabanbaz.feature.auth.data.dto.VerifyCodeRequestDto
import ir.startup.zabanbaz.feature.auth.data.dto.VerifyCodeResponseDto
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class AuthRemoteDataSource(
    private val apiService: ApiService,
) {
    suspend fun requestCode(mobile: String) {
        apiService.post(
            endpoint = "/auth/request-code/",
            body = RequestCodeRequestDto(mobile = mobile),
            skipAuth = true,
        ) { _: JsonObject -> }
    }

    suspend fun verifyCode(mobile: String, code: String): VerifyCodeResponseDto =
        apiService.post(
            endpoint = "/auth/verify/",
            body = VerifyCodeRequestDto(mobile = mobile, code = code),
            skipAuth = true,
        ) { body ->
            HttpClientFactory.defaultJson.decodeFromJsonElement(
                VerifyCodeResponseDto.serializer(),
                body,
            )
        }
}
