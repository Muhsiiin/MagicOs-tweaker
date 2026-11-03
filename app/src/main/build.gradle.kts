plugins {
    id("com.android.application") version "8.3.2"
    id("org.jetbrains.kotlin.android") version "1.9.23"
}

android {
    namespace = "com.muhsiin.magicos_tweaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.muhsiin.magicos_tweaker"
        minSdk = 26
        targetSdk = 34
        // Changing version properties forces GitHub Actions to completely drop old cache states
        versionCode = 2
        versionName = "1.1-CLEAN-BUILD"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // 100% hardcoded strings to fully bypass version catalogs
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Jetpack Compose UI
    implementation("androidx.compose.ui:ui:1.6.5")
    implementation("androidx.compose.ui:ui-graphics:1.6.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.5")
    implementation("androidx.compose.material3:material3:1.2.1")
    
    // Shizuku Native Core
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
}
