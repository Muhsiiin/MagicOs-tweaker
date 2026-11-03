plugins {
    id("com.android.application") version "8.2.2"
    id("org.jetbrains.kotlin.android") version "1.9.22"
}

android {
    namespace = "com.muhsilin.magicos_tweaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.muhsilin.magicos_tweaker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // === THE WORKAROUND OVERRIDE ===
        // This injects core metadata names into the compilation tasks directly,
        // neutralizing the errors on lines 24, 27, 30, and 33 instantly.
        manifestPlaceholders["activityName"] = "com.muhsilin.magicos_tweaker.MainActivity"
        manifestPlaceholders["actionName"] = "android.intent.action.MAIN"
        manifestPlaceholders["categoryName"] = "android.intent.category.LAUNCHER"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
