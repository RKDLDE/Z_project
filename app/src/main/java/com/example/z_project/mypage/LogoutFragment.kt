package com.example.z_project.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentBottomsheetBinding
import com.example.z_project.databinding.FragmentLoginBinding
import com.example.z_project.databinding.FragmentMypageBinding
import com.google.android.material.dialog.MaterialDialogs
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import com.example.z_project.databinding.FragmentLogoutBinding
import com.example.z_project.login.LoginActivity
import com.kakao.sdk.user.UserApiClient

class LogoutFragment(
    context: Context,
) : Dialog(context) { // 뷰를 띄워야하므로 Dialog 클래스는 context를 인자로 받는다.

    private lateinit var binding: FragmentLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = FragmentLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        // 뒤로가기 버튼, 빈 화면 터치를 통해 dialog가 사라지지 않도록
        setCancelable(false)

        // background를 투명하게 만듦
        // (중요) Dialog는 내부적으로 뒤에 흰 사각형 배경이 존재하므로, 배경을 투명하게 만들지 않으면
        // corner radius의 적용이 보이지 않는다.
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // OK Button 클릭에 대한 Callback 처리. 이 부분은 상황에 따라 자유롭게!
        noButton.setOnClickListener {
            dismiss()
        }
        // yesButton 클릭 리스너 추가
        binding.yesButton.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    // 로그아웃 실패
                    Toast.makeText(context, "로그아웃 실패: ${error.message}", Toast.LENGTH_SHORT).show()
                } else {
                    // 로그아웃 성공
                    Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show()

                    // 로그아웃 후 추가 처리 (로그인 화면으로 이동 등)
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)

                    // 다이얼로그 닫기
                    dismiss()
                }
            }
        }
    }
}
