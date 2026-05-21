package ir.startup.zabanbaz.common.languages.data.repository

import ir.startup.zabanbaz.common.languages.data.datasource.LanguagesRemoteDataSource
import ir.startup.zabanbaz.common.languages.domain.Language
import ir.startup.zabanbaz.common.languages.domain.LanguagesRepository

class LanguagesRepositoryImpl(
    private val remoteDataSource: LanguagesRemoteDataSource,
) : LanguagesRepository {
    override suspend fun getLanguages(): List<Language> = remoteDataSource.getLanguages()
}
