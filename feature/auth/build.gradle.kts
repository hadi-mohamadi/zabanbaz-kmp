plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget()
    if (enableIosTargets()) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:errors"))
            implementation(project(":core:networking"))
            implementation(project(":core:presentation"))
            implementation(project(":core:storage"))
            implementation(project(":common:session"))
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "ir.startup.zabanbaz.feature.auth"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
