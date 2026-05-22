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
            implementation(project(":core:storage"))
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "ir.startup.zabanbaz.common.session"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
