package com.example.z_project.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.z_project.MainActivity
import com.example.z_project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlin.random.Random


class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: FirebaseFirestore
    private var uniqueCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()

        uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)
        Log.d("Login","고유코드: {$uniqueCode}")
        if (uniqueCode == null) {
            generateUniqueCode() // 고유 코드가 없으면 발급
        } else{
            // 고유 코드가 있을 경우 리프레시 토큰을 확인
            checkRefreshToken()
        }

//        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)
//        if (refreshToken != null) {
//            refreshAccessToken() // 리프레시 토큰으로 엑세스 토큰 새로 발급
//        } else {
//            kakaologin()
//        }
    }

    private fun generateUniqueCode() {
        // 증복방지를 위헤 Firestore에서 기존 코드 가져오기
        db.collection("users").get().addOnSuccessListener { documents ->
            val existingCodes = documents.map { it.getString("uniqueCode") ?: "" }.toSet() // 기존 고유 코드 집합
            createNewUniqueCode(existingCodes)
        }
    }

    private fun createNewUniqueCode(existingCodes: Set<String>) {
        var code: String
        do {
            code = generateRandomCode()
        } while (existingCodes.contains(code)) // 중복되지 않을 때까지 반복

        // Firestore에 사용자 이름과 프로필 사진, 고유 코드 저장
        saveUserToFirestore("", null, code) // 임시로 사용자 정보를 비워둠

        // 로컬 저장
        sharedPreferences.edit().putString("UNIQUE_CODE", code).apply()
        Log.d("Login","내 고유코드:{$code}")
        Log.d("Login","고유코드 로컬 저장")
    }

    private fun generateRandomCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..12)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }

    private fun checkRefreshToken() {
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)
        if (refreshToken != null) {
            refreshAccessToken() // 리프레시 토큰으로 엑세스 토큰 새로 발급
        } else {
            kakaologin()
        }
    }

    private fun refreshAccessToken() {
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", "")

        Log.d("Login", "로컬 리프레시 토큰: $refreshToken")
        if (refreshToken != null) {
            // 새로 엑세스 토큰 발급 요청
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("로그인 오류", error.message.toString())
                    kakaologin()
                    return@loginWithKakaoTalk
                }

                if (token != null) {
                    // 엑세스 토큰이 새로 발급되었습니다.
                    val newAccessToken = token.accessToken
                    Log.d("새 엑세스 토큰", newAccessToken)
                    Log.e("Login error", error?.message.toString())

                    // 1. 애뮬레이터 용
                    //kakaologin()

                    // 2. 카톡 설치된 안드로이드 용 (애뮬레이터 사용 시 주석처리 부탁)
                    goToMainActivity(token)

                } else if (token != null) {
                    Log.d("Login new AccessToken", token.accessToken)
                }
            }
        } else {
            kakaologin()
        }
    }
    private fun fetchUserFromFirestore(uniqueCode: String, nickname: String?, profileImageUrl: String?) {
        if (uniqueCode.isNotEmpty()) {
            db.collection("users").document(uniqueCode).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // 사용자 정보가 존재할 경우 처리
                        Log.d("Login", "사용자 정보 가져오기 성공: ${document.data}")
                    } else {
                        // 사용자 정보가 없으면 Firestore에 새로 저장
                        saveUserToFirestore(nickname ?: "이름 없음", profileImageUrl, uniqueCode)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Login", "사용자 정보 가져오기 실패", e)
                }
        }
    }
    private fun kakaologin() {
        val kakaoLoginButton = findViewById<ImageButton>(R.id.ib_kakao)
        Log.d("Login", "kakaologin")

        kakaoLoginButton.setOnClickListener {
            // 어플 깔려있는지
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Toast.makeText(this, "카카오톡 로그인 실패, 카카오 계정으로 로그인 시도", Toast.LENGTH_SHORT).show()
                        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                            handleLoginResult(token, error)
                        }
                    } else {
                        handleLoginResult(token, error)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    handleLoginResult(token, error)
                }
            }
        }
    }

    private fun saveUserToFirestore(userName: String, profileImageUrl: String?, uniqueCode: String?) {
        val userId = uniqueCode // 고유 코드를 문서 ID로 사용

        val user = hashMapOf(
            "name" to userName,
            "profileImage" to profileImageUrl,
            "uniqueCode" to uniqueCode
        )

        db.collection("users")
            .document(userId ?: "")
            .set(user)
            .addOnSuccessListener {
                Log.d("Login", "firebase 사용자 정보 저장 성공!")
            }
            .addOnFailureListener { e ->
                Log.w("Login", "firebase 사용자 정보 저장 실패", e)
            }
    }

    private fun handleLoginResult(token: OAuthToken?, error: Throwable?) {
        Log.d("Login", "handleLoginResult 실행")
        if (error != null) {
            Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            Log.e("Login error", error.message.toString())
        } else if (token != null) {
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()

            // 리프레시 토큰 확인 및 저장
            val refreshToken = token.refreshToken
            Log.d("Login", "리프레시 토큰: $refreshToken")

            // SharedPreferences에 리프레시 토큰 저장
            sharedPreferences.edit().putString("REFRESH_TOKEN", refreshToken).apply()
            sharedPreferences.edit().putString("UNIQUE_CODE", uniqueCode).apply()

            // 사용자 정보 요청
            UserApiClient.instance.me { user, error ->
                if (user != null) {
                    val nickname = user.kakaoAccount?.profile?.nickname
                    val profileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl
                    Log.d("Login", "사용자이름: $nickname")
                    Toast.makeText(this, "사용자 이름: $nickname", Toast.LENGTH_SHORT).show()

                    // Firestore에 사용자 이름과 프로필 사진, 고유 코드 저장
                    saveUserToFirestore(nickname ?: "이름 없음", profileImageUrl, sharedPreferences.getString("UNIQUE_CODE", ""))

                    // MainActivity로 이동
                    goToMainActivity(token)
                } else {
                    Log.e("Login error", error?.message.toString())
                }
            }
        }
    }

    private fun goToMainActivity(token: OAuthToken) {
        Log.d("goToMainActivity함수", "실행")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
