plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
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
            implementation(project(":core:presentation"))
            implementation(project(":common:session"))
            implementation(project(":common:profile"))
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "ir.startup.zabanbaz.feature.startup"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
