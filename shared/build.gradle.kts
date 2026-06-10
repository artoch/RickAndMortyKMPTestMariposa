@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelight)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }
    
    androidLibrary {
       namespace = "org.toch.rickmortytest.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
           freeCompilerArgs.add("-Xexpect-actual-classes")
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)

            implementation(libs.androidx.activity.compose)

            // ktor
            implementation(libs.ktor.client.okhttp)

            // Koin
            implementation(libs.koin.android)

            // Splash
            implementation(libs.androidx.core.splashscreen)

            implementation(libs.androidx.paging.compose)

            implementation(libs.sqldelight.android.driver)

        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.androidx.icons.extended)

            implementation(libs.androidx.navigation)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.sqldelight.coroutines.extensions)


            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.paging.compose)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.koin.test)
            implementation("androidx.paging:paging-testing:3.5.0")
            implementation(libs.compose.ui.test)
            implementation("io.coil-kt.coil3:coil-test:3.0.4")
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)

            implementation(libs.sqldelight.native.driver)
        }
        val androidHostTest by getting {
            dependencies {
                implementation(libs.core.ktx)
                implementation(libs.robolectric)
                implementation(libs.roborazzi.compose)
                implementation(libs.roborazzi.compose.preview.scanner.support)
                implementation(libs.roborazzi.junit.rule)
                implementation(libs.composable.preview.scanner.android)
                implementation(libs.androidx.compose.ui.test.junit4)
                implementation(libs.androidx.testExt.junit)
                implementation(libs.koin.test)
                implementation(libs.koin.test.junit4)

                implementation(libs.sqldelight.sqlite.driver)
            }
        }
    }
}

sqldelight {
    databases {
        create("RickAndMortyDatabaseTest") {
            packageName.set("org.toch.rickmortytest.database")
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}