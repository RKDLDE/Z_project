//import org.jetbrains.kotlin.gradle.internal.kapt.incremental.UnknownSnapshot.classpath

//import org.jetbrains.kotlin.gradle.internal.kapt.incremental.UnknownSnapshot.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.41" apply false

    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" apply false


}

//allprojects {
//    repositories{
//        google()
//        jcenter()
//        mavenCentral()
//        maven { url = java.net.URI("https://jitpack.io") }
//    }
//}



buildscript{
    dependencies{
        classpath ("com.google.gms:google-services:4.4.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.41")
    }
}



//task clean(type: Delete) {
//    delete rootProject.buildDir
//}


