plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:errors"))
            implementation(project(":core:networking"))
            implementation(project(":core:storage"))
            implementation(project(":core:presentation"))
            implementation(project(":common:session"))
            implementation(project(":common:profile"))
            implementation(project(":common:languages"))
            implementation(project(":common:placement"))
            implementation(project(":feature:auth"))
            implementation(libs.koin.core)
            implementation(libs.multiplatform.settings)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.lifecycle.viewmodel)
        }
    }
}

android {
    namespace = "ir.startup.zabanbaz.core.di"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
