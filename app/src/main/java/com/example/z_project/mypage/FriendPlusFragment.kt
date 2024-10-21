package com.example.z_project.mypage


import android.os.Bundle
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Toast
import com.example.z_project.databinding.FragmentPlusfriendBinding
import com.example.z_project.databinding.FragmentRenameBinding
import com.google.firebase.firestore.FirebaseFirestore

class FriendPlusFragment(
    context: Context,
) : Dialog(context) { // 뷰를 띄워야하므로 Dialog 클래스는 context를 인자로 받는다.

    private lateinit var binding: FragmentPlusfriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = FragmentPlusfriendBinding.inflate(layoutInflater)
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

        yesButton.setOnClickListener {
            val friendCode = etFriendCode.text.toString().trim() // 입력한 친구 코드 가져오기
            Log.d("FriendPlus", "입력 친구 코드: ${friendCode}")

            if (friendCode.isNotBlank()) {
                // SharedPreferences에서 이미 친구 목록에 있는지 확인
                val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
                val existingFriendName = sharedPreferences.getString(friendCode, null)

                if (existingFriendName != null) {
                    Toast.makeText(context, "이미 친구 목록에 추가된 사용자입니다.", Toast.LENGTH_SHORT).show()
                    Log.d("FriendPlus", "친구 목록에 존재")
                    return@setOnClickListener
                }

                // Firebase에서 friendCode가 존재하는지 확인
                FirebaseFirestore.getInstance().collection("users").document(friendCode)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            // 친구 코드가 존재할 경우, 친구의 정보 가져오기
                            Log.d("FriendPlus", "친구 코드 firebase에 존재")

                            val friendName = document.getString("name") ?: "이름 없음"

                            Log.d("FriendPlus", "검색 결과: ${friendName}")

                            // 친구 코드만 SharedPreferences에 저장
                            val editor = sharedPreferences.edit()
                            editor.putString(friendCode, friendName) // 친구 코드와 이름 저장
                            editor.apply()

                            Toast.makeText(context, "${friendName}(이)가 친구 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            dismiss() // 다이얼로그 닫기
                        } else {
                            Toast.makeText(context, "존재하지 않는 친구 코드입니다.", Toast.LENGTH_SHORT).show()
                            Log.d("FriendPlus", "존재하지 않는 친구 코드")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(context, "친구 코드 조회 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "친구 코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                Log.d("FriendPlus", "다시 입력하시오")
            }
        }
        noButton.setOnClickListener {
            dismiss()
            Log.d("FriendPlus", "Dialog 닫기")
        }
    }
}