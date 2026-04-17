plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.compose)
//    id("com.google.gms.google-services")
//    id("com.google.firebase.crashlytics")
//    id("kotlin-parcelize")  // Add this for @Parcelize support
//    id("org.jetbrains.kotlin.plugin.parcelize")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")

}

android {
    namespace = "com.example.rakshasetu"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.rakshasetu"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Compose Foundation (includes KeyboardOptions)
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.foundation:foundation-layout")
    implementation("androidx.compose.foundation:foundation:1.6.8")
    // Kotlin & Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Android Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
   // implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    implementation("androidx.compose.foundation:foundation-layout")
    implementation("androidx.compose.foundation:foundation")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Firebase BoM - SINGLE INSTANCE ONLY
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))

    // Firebase products
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("io.coil-kt:coil-gif:2.6.0")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.maps.android:maps-compose:2.11.4")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.foundation)

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // For JSON parsing
    implementation("org.json:json:20230227")
    // For Cloudinary
    implementation("com.squareup.okhttp3:okhttp:4.12.0")


    // For image loading
    implementation("io.coil-kt:coil-compose:2.6.0")
//    // Kotlin & Coroutines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
//
//    // Android Core
//    implementation("androidx.core:core-ktx:1.13.1")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
//    implementation("androidx.activity:activity-compose:1.9.2")
//    implementation("androidx.core:core-splashscreen:1.0.1")
//
//    // Compose BOM
//    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.compose.material:material-icons-extended")
//    implementation("androidx.compose.foundation:foundation-layout")
//    implementation("androidx.compose.foundation:foundation")
//
//    // Navigation
//    implementation("androidx.navigation:navigation-compose:2.7.7")
//
//    // Firebase BoM - SINGLE INSTANCE ONLY
//    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
//
//    // Firebase products - NO VERSION NUMBERS NEEDED (BoM handles versions)
//    implementation("com.google.firebase:firebase-auth-ktx")
//    implementation("com.google.firebase:firebase-firestore-ktx")
//    implementation("com.google.firebase:firebase-messaging-ktx")
//    implementation("com.google.firebase:firebase-storage-ktx")
//    implementation("com.google.firebase:firebase-analytics-ktx")
//    implementation("com.google.firebase:firebase-crashlytics-ktx")
//    implementation("com.google.firebase:firebase-database-ktx")
//
//    // Image Loading
//    implementation("io.coil-kt:coil-compose:2.6.0")
//    implementation("io.coil-kt:coil-gif:2.6.0")
//
//    // Google Maps
//    implementation("com.google.android.gms:play-services-maps:18.2.0")
//    implementation("com.google.android.gms:play-services-location:21.0.1")
//    implementation("com.google.maps.android:maps-compose:2.11.4")
//
//    // Networking
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation(libs.androidx.room.ktx)
//    implementation(libs.androidx.compose.foundation)
//
//    // Testing
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//
//    // Lifecycle utilities
//    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
//
//    // Navigation
//    implementation("androidx.navigation:navigation-compose:2.7.7")
//
//
//    // For Parcelize
//    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.9.0")
//
//    // For Firebase
//    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
//    implementation("com.google.firebase:firebase-firestore-ktx")
//    implementation("com.google.firebase:firebase-auth-ktx")
//    implementation("androidx.parcelable:parcelable:1.0.0")
}