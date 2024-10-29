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
import kotlin.random.Random

class FriendPlusFragment(
    context: Context,
) : Dialog(context) { // 뷰를 띄워야하므로 Dialog 클래스는 context를 인자로 받는다.

    private lateinit var binding: FragmentPlusfriendBinding
    private var userId: String? = null

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
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        yesButton.setOnClickListener {
            val friendCode = etFriendCode.text.toString().trim() // 입력한 친구 코드 가져오기
            Log.d("FriendPlus", "입력 친구 코드: ${friendCode}")


            if (friendCode.isNotBlank()) {
                // 내 unique code 가져오기
                val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
                userId = sharedPreferences.getString("UNIQUE_CODE", null)

                // Firebase에서 friendCode가 존재하는지 확인
                FirebaseFirestore.getInstance().collection("users").document(friendCode)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            // 친구 코드가 존재할 경우, 친구의 정보 가져오기
                            Log.d("FriendPlus", "친구 코드 firebase에 존재")

                            // 친구 관계를 Firebase에 추가
                            addFriend(userId!!, friendCode)
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

    private fun addFriend(userId: String, friendCode: String) {
        val db = FirebaseFirestore.getInstance()

        val chatId = generateRandomCode()

        // 현재 사용자와 친구의 관계 추가
        val friendData = hashMapOf(
            "friendCode" to friendCode,
            "chatId" to chatId
        )

        val currentUserFriendData = hashMapOf(
            "friendCode" to userId,
            "chatId" to chatId
        )


//        // 현재 사용자와 친구의 관계 추가
//        val friendData = hashMapOf(
//            "friendCode" to friendCode
//        )
//
//        val currentUserFriendData = hashMapOf(
//            "friendCode" to userId
//        )

        // 현재 사용자 친구 목록에 추가
        db.collection("friends").document(userId)
            .collection("friendsList").document(friendCode)
            .set(friendData)
            .addOnSuccessListener {
                // 친구의 친구 목록에 현재 사용자 추가
                db.collection("friends").document(friendCode)
                    .collection("friendsList").document(userId)
                    .set(currentUserFriendData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "친구가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        dismiss() // 다이얼로그 닫기
                    }
                    .addOnFailureListener { e ->
                        Log.w("FriendPlus", "Error adding friend to friend's list", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("FriendPlus", "Error adding friend to current user's list", e)
            }
    }


    //랜덤 코드 생성 함수
    private fun generateRandomCode(length: Int = 12): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }


}