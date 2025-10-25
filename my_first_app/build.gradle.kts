plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.my_first_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.my_first_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        // ✅ Java 17로 통일 (2025년 현재 Android Studio Koala 권장)
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        // ✅ JVM 타깃도 17로 맞춤
        jvmTarget = "17"
    }

    // ✅ JVM Toolchain (Gradle 8 이상 환경에서 권장)
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}
