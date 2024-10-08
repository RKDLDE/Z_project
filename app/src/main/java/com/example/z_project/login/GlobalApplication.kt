package com.example.z_project.login

import android.app.Application
import android.util.Log
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
        KakaoSdk.init(this, "0085468296286e234b8814817168c6e7")
    }
}