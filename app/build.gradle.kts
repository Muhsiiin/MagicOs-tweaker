import java.io.File

plugins {
    id("com.android.application") version "8.2.2"
    id("org.jetbrains.kotlin.android") version "1.9.22"
}

// === ULTIMATE AGP PRE-BUILD INJECTION ===
// This intercepts the compiler directly and forcefully writes a perfect manifest
// into the temporary build directories, completely overriding the broken file on disk.
project.afterEvaluate {
    tasks.filter { it.name.contains("Manifest", ignoreCase = true) }.forEach { task ->
        task.doFirst {
            val buildManifestDir = File(project.buildDir, "intermediates/packaged_manifests/debug")
            val srcManifestDir = File(project.projectDir, "src/main")
            
            val cleanManifestContent = """
                <?xml version="1.0" encoding="utf-8"?>
                <manifest xmlns:android="http://android.com">
                    <application
                        android:allowBackup="true"
                        android:icon="@mipmap/ic_launcher"
                        android:label="MagicOs-tweaker"
                        android:roundIcon="@mipmap/ic_launcher_round"
                        android:supportsRtl="true"
                        android:theme="@style/Theme.AppCompat.Light.NoActionBar">
                        <activity
                            android:name="com.muhsilin.magicos_tweaker.MainActivity"
                            android:exported="true">
                            <intent-filter>
                                <action android:name="android.intent.action.MAIN" />
                                <category android:name="android.intent.category.LAUNCHER" />
                            </intent-filter>
                        </activity>
                    </application>
                </manifest>
            """.trimIndent()

            // Force override source directories
            File(srcManifestDir, "AndroidManifest.xml").writeText(cleanManifestContent)
            
            // Force override generated/intermediate directories to crush the cache
            if (buildManifestDir.exists()) {
                File(buildManifestDir, "AndroidManifest.xml").writeText(cleanManifestContent)
            }
        }
    }
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
