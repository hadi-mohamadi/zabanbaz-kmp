package ir.startup.zabanbaz.feature.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCodeRequestDto(
    val mobile: String,
)

@Serializable
data class VerifyCodeRequestDto(
    val mobile: String,
    val code: String,
)

@Serializable
data class VerifyCodeResponseDto(
    val access: String,
    val refresh: String,
    val created: Boolean = false,
    val mobile: String,
)
