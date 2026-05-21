package ir.startup.zabanbaz.common.languages.domain

class GetLanguagesUseCase(
    private val repository: LanguagesRepository,
) {
    suspend operator fun invoke(): List<Language> = repository.getLanguages()
}
