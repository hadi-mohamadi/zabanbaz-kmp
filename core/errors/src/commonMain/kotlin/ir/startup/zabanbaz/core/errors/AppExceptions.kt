package ir.startup.zabanbaz.core.errors

/**
 * Base type for failures surfaced to UI and mapped from lower layers (e.g. HTTP).
 * Uses [Exception] (not sealed Error / Result) to match live_app conventions.
 */
abstract class AppException(
    override val message: String,
) : Exception(message) {
    override fun toString(): String = "${this::class.simpleName}(message=$message)"
}

class ConnectivityException(
    message: String,
    val isTimeout: Boolean = false,
) : AppException(message)

class ServerException(
    message: String,
    val statusCode: Int? = null,
) : AppException(message)

class UnauthorizedException(
    message: String = "Unauthorized request",
) : AppException(message)

class UnknownException(
    message: String,
    val underlyingCause: Throwable? = null,
) : AppException(message)

class RequestCancelledException(
    message: String = "Request cancelled",
) : AppException(message)

// --- Repository / HTTP mapping ---

open class RepositoryException(message: String) : AppException(message) {
    fun getErrorMessage(): String? = message
}

class ParseDataException(message: String) : RepositoryException(message)

class UnKnownException(message: String) : RepositoryException(message)

open class RedirectionErrorException(message: String) : RepositoryException(message)

class NotModifiedErrorException(message: String) : RedirectionErrorException(message)

class UnKnownRedirectionErrorException(message: String) : RedirectionErrorException(message)

open class ClientErrorException(message: String) : RepositoryException(message)

class BadRequestErrorException(message: String) : ClientErrorException(message)

class UnauthorizedErrorException(message: String) : ClientErrorException(message)

class NotFoundErrorException(message: String) : ClientErrorException(message)

class NotAcceptableErrorException(message: String) : ClientErrorException(message)

class UnKnownClientErrorException(message: String) : ClientErrorException(message)
