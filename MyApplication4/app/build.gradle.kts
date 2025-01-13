import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.android.application)
    id("com.google.protobuf") // Plugin de Protobuf
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

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.17.3"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.41.0"
        }
        id("javalite") {
            artifact = "com.google.protobuf:protoc-gen-javalite:3.17.3"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc") {}
                id("javalite") {}
            }
        }
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
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.grpc.okhttp.v1410)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.grpc.stub)
    implementation(libs.annotations.api)
    implementation(libs.protostuff.runtime)
    implementation(libs.protostuff.api)
}

