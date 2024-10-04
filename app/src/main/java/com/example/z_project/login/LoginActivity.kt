package com.example.z_project.login

import android.content.Intent  // 추가된 import
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
        setContentView(R.layout.fragment_login) // 레이아웃 파일 이름 확인

        val kakaoLoginButton = findViewById<ImageButton>(R.id.ib_kakao)

        kakaoLoginButton.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오 계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Toast.makeText(this, "카카오톡 로그인 실패, 카카오 계정으로 로그인 시도", Toast.LENGTH_SHORT).show()
                        // 카카오톡 로그인 실패 시 카카오 계정 로그인 시도
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
                    val profileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl // 프로필 사진 URL
                    Log.d("사용자 이름", "$nickname")
                    Toast.makeText(this, "사용자 이름: $nickname", Toast.LENGTH_SHORT).show()

                    // 로그인 성공 시 MainActivity로 이동
                    val intent = Intent(this, MainActivity::class.java).apply {
//                        putExtra("USER_NAME", nickname)
//                        putExtra("PROFILE_IMAGE", profileImageUrl)
//                        putExtra("TOKEN", token.accessToken) // AccessToken도 전달
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
