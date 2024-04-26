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
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.cicerone)
    implementation(libs.dagger)
    implementation(libs.elmslie.android)
    implementation(libs.elmslie.core)
    implementation(libs.facebook.shimmer)
    implementation(libs.fragment)
    implementation(libs.google.android.flexbox)
    implementation(libs.kirich1409.viewbindingpropertydelegate)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.material)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.interceptor)
    implementation(libs.picasso)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.serialization)
    implementation(libs.viewpager2)
}