import org.gradle.api.Project

/**
 * When false (default), iOS Kotlin/Native targets are not registered — no kotlin-native-prebuilt download.
 * Set `zabanbaz.enableNativeCommonizer=true` on macOS for iOS builds.
 */
fun Project.enableIosTargets(): Boolean =
    providers.gradleProperty("zabanbaz.enableNativeCommonizer")
        .map { it.equals("true", ignoreCase = true) }
        .getOrElse(false)
