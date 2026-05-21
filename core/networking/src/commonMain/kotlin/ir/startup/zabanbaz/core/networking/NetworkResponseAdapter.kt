package ir.startup.zabanbaz.core.networking

import ir.startup.zabanbaz.core.errors.BadRequestErrorException
import ir.startup.zabanbaz.core.errors.ClientErrorException
import ir.startup.zabanbaz.core.errors.ConnectivityException
import ir.startup.zabanbaz.core.errors.NotAcceptableErrorException
import ir.startup.zabanbaz.core.errors.NotFoundErrorException
import ir.startup.zabanbaz.core.errors.NotModifiedErrorException
import ir.startup.zabanbaz.core.errors.RedirectionErrorException
import ir.startup.zabanbaz.core.errors.ServerException
import ir.startup.zabanbaz.core.errors.UnKnownClientErrorException
import ir.startup.zabanbaz.core.errors.UnKnownException
import ir.startup.zabanbaz.core.errors.UnKnownRedirectionErrorException
import ir.startup.zabanbaz.core.errors.UnauthorizedErrorException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object NetworkResponseAdapter {
    private val parseJson = Json { ignoreUnknownKeys = true }

    fun messageFromBody(body: String?): String {
        if (body.isNullOrBlank()) return "Request failed"
        return runCatching {
            val element = parseJson.parseToJsonElement(body)
            if (element is JsonObject) {
                element["detail"]?.jsonPrimitive?.contentOrNull?.takeIf { it.isNotEmpty() }
                    ?: element["message"]?.jsonPrimitive?.contentOrNull?.takeIf { it.isNotEmpty() }
            } else {
                null
            }
        }.getOrNull() ?: "Request failed"
    }

    fun throwForResponseException(
        exception: ResponseException,
        responseBody: String? = null,
    ): Nothing {
        val status = exception.response.status.value
        val message = messageFromBody(responseBody)

        when {
            status in 300..399 -> throwRedirection(status, message)
            status in 400..499 -> throwClient(status, message)
            else -> throw ServerException(message, statusCode = status)
        }
    }

    fun throwForThrowable(throwable: Throwable): Nothing {
        when (throwable) {
            is ConnectivityException,
            is ServerException,
            is ClientErrorException,
            is RedirectionErrorException,
            -> throw throwable
            is ConnectTimeoutException,
            is SocketTimeoutException,
            -> throw ConnectivityException(throwable.message ?: "Connection timed out", isTimeout = true)
            is IOException -> throw ConnectivityException("No internet connection")
            else -> throw UnKnownException(throwable.message ?: "Unknown error")
        }
    }

    private fun throwRedirection(code: Int, message: String): Nothing {
        when (code) {
            304 -> throw NotModifiedErrorException(message)
            else -> throw UnKnownRedirectionErrorException(message)
        }
    }

    private fun throwClient(code: Int, message: String): Nothing {
        when (code) {
            400 -> throw BadRequestErrorException(message)
            401 -> throw UnauthorizedErrorException(message)
            404 -> throw NotFoundErrorException(message)
            406 -> throw NotAcceptableErrorException(message)
            else -> throw UnKnownClientErrorException(message)
        }
    }
}

fun HttpStatusCode.isSuccess(): Boolean = value in 200..299
