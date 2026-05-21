package ir.startup.zabanbaz.core.networking

import ir.startup.zabanbaz.core.errors.ConnectivityException
import ir.startup.zabanbaz.core.errors.ParseDataException
import ir.startup.zabanbaz.core.errors.UnauthorizedErrorException
import ir.startup.zabanbaz.core.errors.UnauthorizedException
import ir.startup.zabanbaz.core.storage.AppSettingsStorage
import ir.startup.zabanbaz.core.storage.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class ApiService(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val tokenStorage: TokenStorage,
    private val appSettingsStorage: AppSettingsStorage,
    private val json: Json = HttpClientFactory.defaultJson,
) {
    private val refreshMutex = Mutex()

    suspend fun <T> get(
        endpoint: String,
        skipAuth: Boolean = false,
        query: Map<String, String> = emptyMap(),
        mapper: suspend (JsonObject) -> T,
    ): T = execute(skipAuth = skipAuth) { mark ->
        val response = httpClient.get(url(endpoint, query)) {
            applyAuth(skipAuth, mark)
        }
        mapResponse(response, mapper)
    }

    suspend fun <T> getJsonArray(
        endpoint: String,
        skipAuth: Boolean = false,
        query: Map<String, String> = emptyMap(),
        mapper: suspend (JsonArray) -> T,
    ): T = execute(skipAuth = skipAuth) { mark ->
        val response = httpClient.get(url(endpoint, query)) {
            applyAuth(skipAuth, mark)
        }
        mapJsonArrayResponse(response, mapper)
    }

    suspend fun <T> post(
        endpoint: String,
        body: Any? = null,
        skipAuth: Boolean = false,
        mapper: suspend (JsonObject) -> T,
    ): T = execute(skipAuth = skipAuth) { mark ->
        val response = httpClient.post(url(endpoint)) {
            applyAuth(skipAuth, mark)
            if (body != null) setBody(body)
        }
        mapResponse(response, mapper)
    }

    suspend fun <T> patch(
        endpoint: String,
        body: Any,
        skipAuth: Boolean = false,
        mapper: suspend (JsonObject) -> T,
    ): T = execute(skipAuth = skipAuth) { mark ->
        val response = httpClient.patch(url(endpoint)) {
            applyAuth(skipAuth, mark)
            setBody(body)
        }
        mapResponse(response, mapper)
    }

    suspend fun delete(
        endpoint: String,
        skipAuth: Boolean = false,
    ): Unit = execute(skipAuth = skipAuth) { mark ->
        val response = httpClient.delete(url(endpoint)) {
            applyAuth(skipAuth, mark)
        }
        if (!response.status.isSuccess()) {
            throw ResponseException(response, response.bodyAsText())
        }
    }

    private suspend fun <T> execute(
        skipAuth: Boolean,
        block: suspend (retriedAfterRefresh: Boolean) -> T,
    ): T {
        var refreshedAfter401 = false
        try {
            return block(false)
        } catch (e: ResponseException) {
            if (!skipAuth && e.response.status.value == 401 && !refreshedAfter401) {
                refreshedAfter401 = true
                refreshAccessToken()
                return block(true)
            }
            NetworkResponseAdapter.throwForResponseException(e, e.response.bodyAsText())
        } catch (e: IOException) {
            throw ConnectivityException("No internet connection")
        } catch (e: Throwable) {
            NetworkResponseAdapter.throwForThrowable(e)
        }
    }

    private suspend fun refreshAccessToken() {
        refreshMutex.withLock {
            val refresh = tokenStorage.readRefreshToken()
                ?: throw UnauthorizedException("Missing refresh token")
            val response = httpClient.post(url("/auth/token/refresh/")) {
                setBody(buildJsonObject { put("refresh", JsonPrimitive(refresh)) })
            }
            if (!response.status.isSuccess()) {
                tokenStorage.clearTokens()
                throw UnauthorizedErrorException(
                    NetworkResponseAdapter.messageFromBody(response.bodyAsText()),
                )
            }
            val body = json.parseToJsonElement(response.bodyAsText()).jsonObject
            val access = body["access"]?.toString()?.trim('"')
                ?: throw ParseDataException("Invalid token response")
            val newRefresh = body["refresh"]?.toString()?.trim('"')
            tokenStorage.writeTokens(access, newRefresh ?: refresh)
        }
    }

    private suspend fun <T> mapResponse(
        response: HttpResponse,
        mapper: suspend (JsonObject) -> T,
    ): T {
        if (!response.status.isSuccess()) {
            throw ResponseException(response, response.bodyAsText())
        }
        val text = response.bodyAsText()
        if (text.isBlank()) {
            return mapper(buildJsonObject { })
        }
        return try {
            val element = json.parseToJsonElement(text)
            mapper(element.jsonObject)
        } catch (e: Exception) {
            throw ParseDataException(e.message ?: "Failed to parse response")
        }
    }

    private suspend fun <T> mapJsonArrayResponse(
        response: HttpResponse,
        mapper: suspend (JsonArray) -> T,
    ): T {
        if (!response.status.isSuccess()) {
            throw ResponseException(response, response.bodyAsText())
        }
        val text = response.bodyAsText()
        return try {
            val element = if (text.isBlank()) {
                json.parseToJsonElement("[]")
            } else {
                json.parseToJsonElement(text)
            }
            mapper(element.jsonArray)
        } catch (e: Exception) {
            throw ParseDataException(e.message ?: "Failed to parse response")
        }
    }

    private suspend fun HttpRequestBuilder.applyAuth(skipAuth: Boolean, retried: Boolean) {
        if (retried) {
            attributes.put(RetryMarker, true)
        }
        header(HttpHeaders.AcceptLanguage, appSettingsStorage.acceptLanguageHeader())
        if (skipAuth) return
        val token = tokenStorage.readAccessToken()
        if (!token.isNullOrEmpty()) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    private fun url(endpoint: String, query: Map<String, String> = emptyMap()): String {
        val path = if (endpoint.startsWith("/")) endpoint else "/$endpoint"
        val base = baseUrl.trimEnd('/')
        val queryString = if (query.isEmpty()) {
            ""
        } else {
            "?" + query.entries.joinToString("&") { "${it.key}=${it.value}" }
        }
        return "$base$path$queryString"
    }

    private companion object {
        val RetryMarker = io.ktor.util.AttributeKey<Boolean>("retried_after_refresh")
    }
}
