plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)


    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.z_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.z_project"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
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
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildToolsVersion = "34.0.0"

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.emoji2.emojipicker)
    implementation(libs.firebase.crashlytics.buildtools)

    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // ViewPager2
    implementation ("androidx.viewpager2:viewpager2:1.1.0-beta01")

    //캘린더 라이브러리
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")


    //이미지 접근 권한
    implementation("androidx.activity:activity-ktx:1.5.1")
    implementation("androidx.fragment:fragment-ktx:1.5.2")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))


    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    implementation ("com.google.firebase:firebase-auth")

    //업로드 이모티콘
//    implementation("androidx.emoji2:emojipicker:$version")



    //이모지
    implementation("com.google.guava:guava:31.1-android")
    implementation("androidx.emoji2:emoji2-emojipicker:1.5.0")

    implementation("androidx.emoji2:emoji2:1.1.0")
    implementation("androidx.emoji2:emoji2-bundled:1.1.0")


    //카카오톡
    implementation("com.kakao.sdk:v2-all:2.12.0")
    implementation("com.kakao.sdk:v2-user:2.12.0") // 카카오 로그인
    implementation("com.kakao.sdk:v2-talk:2.12.0") // 친구, 메시지(카카오톡)
    implementation("com.kakao.sdk:v2-link:2.12.0") // 메시지(카카오링크)


    //채팅
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-compiler:2.45")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")

    //테드 퍼미션
    /*implementation("gun0912.ted:tedpermission:2.2.3")*/
}
