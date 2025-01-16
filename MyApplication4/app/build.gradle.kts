plugins {
    alias(libs.plugins.android.application)
    id("com.google.protobuf") version "0.9.3" // Plugin de Protobuf
}

android {
    namespace = "com.example.myapplication4"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication4"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")       // Retrofit core
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")   // Gson converter (si usas JSON)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation(libs.media3.common)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
<<<<<<< Updated upstream
=======

    implementation("io.grpc:grpc-okhttp:1.68.1")
    implementation("io.grpc:grpc-protobuf-lite:1.68.1")
    implementation("io.grpc:grpc-stub:1.68.1")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

>>>>>>> Stashed changes
}

