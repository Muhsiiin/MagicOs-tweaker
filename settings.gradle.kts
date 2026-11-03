import java.io.File

// === ULTRA PRIMITIVE SELF-HEALING HOOK ===
// This executes during initialization before ANY plugin can scan files.
val manifestDir = File(rootDir, "app/src/main")
if (!manifestDir.exists()) {
    manifestDir.mkdirs()
}
val manifestFile = File(manifestDir, "AndroidManifest.xml")
manifestFile.writeText("""
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
""".trimIndent())

// Standard repositories configuration block
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MagicOs-tweaker"
include(":app")
