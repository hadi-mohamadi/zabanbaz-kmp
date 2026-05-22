enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        maven { url = uri("https://cache-redirector.jetbrains.com/maven-central") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        maven { url = uri("https://cache-redirector.jetbrains.com/maven-central") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "zabanbaz"
include(":androidApp")
include(":composeApp")
include(":core:errors")
include(":core:presentation")
include(":core:storage")
include(":core:networking")
include(":core:di")
include(":common:session")
include(":common:languages")
include(":common:profile")
include(":common:placement")
include(":feature:auth")
include(":feature:startup")
include(":feature:onboarding")
include(":feature:placement")
include(":feature:home")
include(":feature:profile-ui")