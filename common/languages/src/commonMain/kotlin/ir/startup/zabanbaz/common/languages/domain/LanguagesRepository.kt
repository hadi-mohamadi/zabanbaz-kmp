package ir.startup.zabanbaz.common.languages.domain

interface LanguagesRepository {
    suspend fun getLanguages(): List<Language>
}
