package ir.startup.zabanbaz.feature.auth.domain

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val mobile: String,
    val created: Boolean,
)
