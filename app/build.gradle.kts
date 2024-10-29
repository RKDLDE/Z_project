plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)


    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

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
        vectorDrawables {
            useSupportLibrary = true
        }
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
        implementation("androidx.viewpager2:viewpager2:1.1.0-beta01")

        //캘린더 라이브러리
        implementation("com.github.prolificinteractive:material-calendarview:2.0.1")


        //이미지 접근 권한
        implementation("androidx.activity:activity-ktx:1.5.1")
        implementation("androidx.fragment:fragment-ktx:1.5.2")

        // Import the Firebase BoM
        implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
        // Firebase Storage 의존성 추가
        implementation("com.google.firebase:firebase-storage")
        implementation("com.google.firebase:firebase-database-ktx:20.0.4")

        // When using the BoM, don't specify versions in Firebase dependencies
        implementation("com.google.firebase:firebase-analytics")


        implementation("com.google.firebase:firebase-auth")

        //firebase 알림
        implementation(libs.firebase.messaging)
        implementation("com.google.firebase:firebase-analytics-ktx")
        implementation("com.google.firebase:firebase-messaging:23.0.3")
/*        implementation(libs.androidx.databinding.runtime)*/
        /*implementation(libs.androidx.library)*/
        implementation(libs.androidx.work.runtime.ktx)


        //이모지
        implementation("com.google.guava:guava:31.1-android")
        implementation("androidx.emoji2:emoji2-emojipicker:1.5.0")

        implementation("androidx.emoji2:emoji2:1.1.0")
        implementation("androidx.emoji2:emoji2-bundled:1.1.0")


        //카카오톡
        implementation("com.kakao.sdk:v2-common:2.20.6")
        implementation("com.kakao.sdk:v2-all:2.20.6") // 전체 모듈 설치, 2.11.0 버전부터 지원
        implementation("com.kakao.sdk:v2-user:2.20.6") // 카카오 로그인 API 모듈
        implementation("com.kakao.sdk:v2-share:2.20.6") // 카카오톡 공유 API 모듈
        implementation("com.kakao.sdk:v2-talk:2.20.6") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
//    implementation "com.kakao.sdk:v2-friend:2.20.0" // 피커 API 모듈
//    implementation "com.kakao.sdk:v2-navi:2.20.0" // 카카오내비 API 모듈
//    implementation ("com.kakao.sdk:v2-cert:2.20.0") // 카카오톡 인증 서비스 API 모듈


        //채팅
        //implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
        implementation("com.google.dagger:hilt-android:2.51.1")
        kapt("com.google.dagger:hilt-compiler:2.51.1")
        implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
        implementation(libs.androidx.navigation.compose)

        implementation("io.coil-kt:coil-compose:2.4.0")


        implementation("de.hdodenhof:circleimageview:3.1.0")



        //테드 퍼미션
        /*implementation("gun0912.ted:tedpermission:2.2.3")*/

        implementation("com.github.bumptech.glide:glide:4.12.0")
        implementation("com.github.bumptech.glide:annotations:4.12.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    }



}
