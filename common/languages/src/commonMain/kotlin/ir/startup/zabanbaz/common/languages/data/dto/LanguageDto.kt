package ir.startup.zabanbaz.common.languages.data.dto

import ir.startup.zabanbaz.common.languages.domain.Language
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguageDto(
    val id: Int,
    val code: String,
    val name: String,
    @SerialName("native_name") val nativeName: String,
) {
    fun toDomain(): Language = Language(
        id = id,
        code = code,
        name = name,
        nativeName = nativeName,
    )
}
