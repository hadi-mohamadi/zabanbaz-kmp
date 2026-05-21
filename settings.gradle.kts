enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "zabanbaz"
include(":androidApp")
include(":composeApp")
include(":shared")
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