package ir.startup.zabanbaz.common.profile.domain

data class UserProfile(
    val phone: String = "",
    val sex: String? = null,
    val learningLanguageId: Int? = null,
    val learningLanguageCode: String? = null,
    val learningLanguageName: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val englishCefrLevel: String? = null,
) {
    val isCoreComplete: Boolean
        get() = !sex.isNullOrBlank() &&
            learningLanguageId != null &&
            !username.isNullOrBlank()

    /** First-time routing: English learners without a CEFR level must take placement. */
    val needsEnglishPlacement: Boolean
        get() = learningLanguageCode.equals("en", ignoreCase = true) &&
            englishCefrLevel.isNullOrBlank()

    /** English learners can retake placement from home to update their level. */
    val canRetakeEnglishPlacement: Boolean
        get() = learningLanguageCode.equals("en", ignoreCase = true)

    val fullName: String?
        get() = listOfNotNull(firstName?.trim(), lastName?.trim())
            .filter { it.isNotEmpty() }
            .joinToString(" ")
            .takeIf { it.isNotEmpty() }
}
