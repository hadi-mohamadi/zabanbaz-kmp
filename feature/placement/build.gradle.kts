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
            implementation(project(":core:errors"))
            implementation(project(":core:presentation"))
            implementation(project(":common:placement"))
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "ir.startup.zabanbaz.feature.placement"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
