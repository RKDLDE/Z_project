package com.example.z_project.login

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 다른 초기화 코드들
        Log.d("GlobalApplication", "onCreate called")
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        // Kakao SDK 초기화
        KakaoSdk.init(this, "kakaSdk")

        // Firebase 초기화
        FirebaseApp.initializeApp(this)
    }
}
