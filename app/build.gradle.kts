plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")

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
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
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
    //implementation(libs.androidx.monitor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
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

    // 채팅
    implementation("com.google.dagger:hilt-android:2.41")
    kapt("com.google.dagger:hilt-android-compiler:2.41")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")


    implementation(libs.androidx.navigation.compose)
}


    //테드 퍼미션
    /*implementation("gun0912.ted:tedpermission:2.2.3")*/


