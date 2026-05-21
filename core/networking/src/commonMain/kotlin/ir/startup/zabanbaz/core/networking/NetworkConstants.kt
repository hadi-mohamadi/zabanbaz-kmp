package ir.startup.zabanbaz.core.networking

object NetworkConstants {
    /** API host (no trailing slash). */
    const val API_HOST = "https://zabandar.ir"

    const val API_PREFIX = "/api/v1"

    /** Full API base URL used for all HTTP calls, e.g. `https://zabandar.ir/api/v1`. */
    const val API_BASE_URL = "$API_HOST$API_PREFIX"

    const val SKIP_AUTH_ATTRIBUTE = "skipAuth"
}
