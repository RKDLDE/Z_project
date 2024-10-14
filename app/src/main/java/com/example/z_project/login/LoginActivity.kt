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

class LoginActivity : AppCompatActivity() {
    // SharedPreferences 객체 선언
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        //sharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)

        // 리프레시 토큰 가져오기
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)

        if (refreshToken != null) {
            refreshAccessToken()  // 리프레시 토큰으로 엑세스 토큰 새로 발급
        } else {
            kakaologin()
        }
    }
    private fun refreshAccessToken() {
        val refreshToken = sharedPreferences.getString("REFRESH_TOKEN", null)
        Log.d("저장된 리프레시 토큰", "Refresh Token: $refreshToken")
        Log.d("refreshAccessToken함수", "실행")
        if (refreshToken != null) {
            // 새로 엑세스 토큰 발급 요청
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("로그인 오류", error.message.toString())
                    kakaologin()
                }

                if (token != null) {
                    // 엑세스 토큰이 새로 발급되었습니다.
                    val newAccessToken = token.accessToken
                    Log.d("새 엑세스 토큰", newAccessToken)

                    UserApiClient.instance.me { user, error ->
                        if (user != null) {
                            val nickname = user.kakaoAccount?.profile?.nickname

                            // MainActivity로 이동하며 사용자 정보 전달
                            goToMainActivity(token, nickname ?: "이름 없음") // 사용자 이름도 함께 전달
                        } else {
                            Log.e("사용자 정보 오류", error?.message.toString())
                        }
                    }
                }
            }
        }
        else{
            kakaologin()
        }
    }

    private fun kakaologin() {
        val kakaoLoginButton = findViewById<ImageButton>(R.id.ib_kakao)
        Log.d("kakaologin함수", "실행")
        kakaoLoginButton.setOnClickListener {
            //어플 깔려있는지
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



    // firebase 저장 함수
    private fun saveUserToFirestore(userName: String, refreshToken: String) {
        val db = FirebaseFirestore.getInstance()
        val userId = refreshToken

        val user = hashMapOf(
            "name" to userName,
            "r_token" to refreshToken
        )

        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "사용자 정보 저장 성공!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "사용자 정보 저장 실패", e)
            }
    }

    private fun handleLoginResult(token: OAuthToken?, error: Throwable?) {
        Log.d("handleLoginResult함수", "실행")
        if (error != null) {
            Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            Log.e("로그인 에러", error.message.toString())
        } else if (token != null) {
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()

            // 리프레시 토큰 확인 및 저장
            val refreshToken = token.refreshToken
            Log.d("리프레시 토큰 저장하기", "Refresh Token: $refreshToken")

            UserApiClient.instance.me { user, error ->
                if (user != null) {
                    val nickname = user.kakaoAccount?.profile?.nickname
                    val profileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl
                    Log.d("사용자 이름", "$nickname")
                    Toast.makeText(this, "사용자 이름: $nickname", Toast.LENGTH_SHORT).show()

                    // Firestore에 사용자 이름 저장
                    //saveUserToFirestore(nickname ?: "이름 없음", refreshToken)

                    // SharedPreferences에 리프레시 토큰 저장
                    sharedPreferences.edit().putString("REFRESH_TOKEN", refreshToken).apply()

                    // MainActivity로 이동하며 사용자 정보 전달
                    goToMainActivity(token, nickname ?: "이름 없음")


//                    val intent = Intent(this, MainActivity::class.java).apply {
//                        putExtra("USER_NAME", nickname)
//                        putExtra("PROFILE_IMAGE", profileImageUrl)
//                        putExtra("TOKEN", token.accessToken)
//                    }
//                    startActivity(intent)
//
//                    // LoginActivity 종료
//                    finish()
                } else {
                    Log.e("사용자 정보 오류", error?.message.toString())
                }
            }
        }
    }
    private fun goToMainActivity(token: OAuthToken, userName: String) {
        Log.d("goToMainActivity함수", "실행")
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_NAME", userName) // 사용자 이름을 여기서 전달 필요
            putExtra("TOKEN", token.accessToken)
        }
        startActivity(intent)
        finish()
    }
}
