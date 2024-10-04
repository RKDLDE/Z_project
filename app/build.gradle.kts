plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)


    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
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

    implementation ("com.kakao.sdk:v2-all:2.20.0") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation ("com.kakao.sdk:v2-user:2.20.0") // 카카오 로그인 API 모듈
    implementation ("com.kakao.sdk:v2-share:2.20.0") // 카카오톡 공유 API 모듈
//    implementation "com.kakao.sdk:v2-talk:2.20.0" // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
//    implementation "com.kakao.sdk:v2-friend:2.20.0" // 피커 API 모듈
//    implementation "com.kakao.sdk:v2-navi:2.20.0" // 카카오내비 API 모듈
//    implementation ("com.kakao.sdk:v2-cert:2.20.0") // 카카오톡 인증 서비스 API 모듈

}
