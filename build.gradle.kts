// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}


//뭔지 모르겠지만...
//buildscript {
//    dependencies {
//        classpath("com.android.tools.build:gradle:8.0.0") // Android Gradle Plugin
//    }
//}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}