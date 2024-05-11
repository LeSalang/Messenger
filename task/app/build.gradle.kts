plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.lesa.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lesa.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
    testOptions {
        unitTests.all { it.useJUnitPlatform() }
        animationsDisabled = true
    }
}

dependencies {
    // Android
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.fragment)
    implementation(libs.google.android.flexbox)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.material)
    implementation(libs.viewpager2)
    implementation(libs.serialization)

    // Cicerone
    implementation(libs.cicerone)

    // Dagger
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // ELM
    implementation(libs.elmslie.android)
    implementation(libs.elmslie.core)

    // Shimmer
    implementation(libs.facebook.shimmer)

    // ViewBindingPropertyDelegate
    implementation(libs.kirich1409.viewbindingpropertydelegate)

    // Retrofit
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.interceptor)
    implementation(libs.picasso)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)

    // Room
    implementation(libs.room)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // JUnit
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // Kotest
    testImplementation(libs.kotest.junit)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)

    // MockK
    testImplementation(libs.mockk)
}