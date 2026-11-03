# MagicOS Tweaker

An Android application built using Jetpack Compose and Kotlin DSL designed to customize global system settings and animation speeds on Honor's MagicOS via the Shizuku IPC binder wrapper interface.

## Features
- **Real-time Shizuku Connectivity:** Automated binder lifecycle tracking to safely verify shell permission status.
- **System Animation Controls:** Precision adjustment sliders to calibrate window, transition, and animator duration scales.
- **Preset Toggles:** Quick options to immediately eliminate animation delay or restore default configurations securely.

## Technical Architecture
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Permissions Framework:** Shizuku API (Inter-Process Communication wrapper)
- **Build Infrastructure:** Gradle Version Catalog (`libs.versions.toml`) with Kotlin DSL configurations

## Project Initialization
To build this package locally:
1. Clone this repository into Android Studio.
2. Ensure your target physical Android device has the **Shizuku** manager app installed and running via wireless debugging or root.
3. Deploy the application package and grant operational shell privileges via the pop-up prompt.
