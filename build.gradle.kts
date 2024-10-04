//import org.jetbrains.kotlin.gradle.internal.kapt.incremental.UnknownSnapshot.
// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.41" apply false
}

//allprojects {
//    repositories{
//        google()
//        jcenter()
//        mavenCentral()
//        maven { url = java.net.URI("https://jitpack.io") }
//    }
//}



//뭔지 모르겠지만...
//buildscript {
//    dependencies {
//        classpath("com.android.tools.build:gradle:8.0.0") // Android Gradle Plugin
//    }
//}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}

