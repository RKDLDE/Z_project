package com.example.z_project.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.z_project.MainActivity
import com.example.z_project.R
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        val kakaoLoginButton = findViewById<ImageButton>(R.id.ib_kakao)

        kakaoLoginButton.setOnClickListener {
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
                    Log.d("카카오계정로그인", "확인용")
                    Log.d("로그인 토큰", "토큰: $token")
                    Log.d("로그인 에러", "에러: $error")
                    handleLoginResult(token, error)
                }
            }
        }
    }

    private fun handleLoginResult(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Toast.makeText(this, "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            Log.e("로그인 에러", error.message.toString())
        } else if (token != null) {
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
            UserApiClient.instance.me { user, error ->
                if (user != null) {
                    val nickname = user.kakaoAccount?.profile?.nickname
                    val profileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl
                    Log.d("사용자 이름", "$nickname")
                    Toast.makeText(this, "사용자 이름: $nickname", Toast.LENGTH_SHORT).show()

                    // MainActivity로 이동하며 사용자 정보 전달
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("USER_NAME", nickname)
                        putExtra("PROFILE_IMAGE", profileImageUrl)
                        putExtra("TOKEN", token.accessToken)
                    }
                    startActivity(intent)

                    // LoginActivity 종료
                    finish()
                } else {
                    Log.e("사용자 정보 오류", error?.message.toString())
                }
            }
        }
    }
}
