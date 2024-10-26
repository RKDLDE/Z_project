package com.example.z_project.mypage

import FriendFragment
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.z_project.MainActivity
import com.example.z_project.R
import com.example.z_project.databinding.FragmentMypageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream
import java.net.URL

class MypageFragment : Fragment(), BottomsheetFragment.ImageSelectionListener {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: FragmentMypageBinding
    private lateinit var tv_name: TextView
    private lateinit var profileImageView: ImageView
    private val firestore = FirebaseFirestore.getInstance()
    private var userId: String? = null
    private val storage = FirebaseStorage.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("UNIQUE_CODE", null)
        Log.d("Mypage", "로컬 userId: $userId")

        tv_name = binding.tvName // TV 이름 초기화
        profileImageView = binding.ivProfile // ImageView 초기화

        // 사용자 정보 가져오기
        if (userId != null) {
            fetchUserInfo()
        } else {
            Log.e("Mypage", "userId가 null입니다.")
        }
//        // 사용자 이름과 프로필 사진 설정
//        if (!userName.isNullOrEmpty()) {
//            tv_name.text = userName
//        }
//
//        // 프로필 이미지 로드
//        if (!profileImageUrl.isNullOrEmpty()) {
//            //Log.d("기본프로필~","${profileImageUrl}")
//            if (profileImageUrl == "https://img1.kakaocdn.net/thumb/R110x110.q70/?fname=https://t1.kakaocdn.net/account_images/default_profile.jpeg") {
//                profileImageView.setImageResource(R.drawable.profile) // 기본 이미지
//            }
//            else{
//                loadProfileImage(profileImageUrl)
//            }
//        } else {
//            profileImageView.setImageResource(R.drawable.profile) // 기본 이미지
//        }

        // 친구 관리 버튼 클릭
        binding.llFriend.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, FriendFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        // 버튼 초기화 및 리스너 설정
        val changeProfileButton: ImageButton = view.findViewById(R.id.ib_change_profile)
        changeProfileButton.setOnClickListener {
            // BottomSheetFragment 호출
            val bottomSheetFragment = BottomsheetFragment()
            bottomSheetFragment.setImageSelectionListener(this) // Listener 설정
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }

        val logoutButton: LinearLayout = view.findViewById(R.id.ll_logout)
        logoutButton.setOnClickListener {
            showLogoutDialog()
        }

        val deleteButton: LinearLayout = view.findViewById(R.id.ll_delete)
        deleteButton.setOnClickListener {
            showDeleteDialog()
        }

        val renameButton: ImageButton = view.findViewById(R.id.ib_rename)
        renameButton.setOnClickListener {
            showRenameDialog()
        }
    }

//    private fun fetchUserInfo() {
//        firestore.collection("users").document(userId!!).get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    val userName = document.getString("name")
//                    val profileImage = document.getString("profileImage")
//
//                    // 사용자 이름과 프로필 사진 설정
//                    tv_name.text = userName ?: "이름 없음"
//                    Log.d("Mypage", "이름 불러오기: ${userName}")
//
//                    if (!profileImage.isNullOrEmpty()) {
//                        loadProfileImage(profileImage)
//                    } else {
//                        profileImageView.setImageResource(R.drawable.profile) // 기본 이미지
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                // 데이터 가져오기 실패 처리
//                Log.e("Mypage", "데이터 가져오기 실패", exception)
//            }
//    }
    private fun fetchUserInfo() {
        firestore.collection("users").document(userId!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.getString("name")
                    val profileImage = document.getString("profileImage")

                    // 사용자 이름과 프로필 사진 설정
                    tv_name.text = userName ?: "이름 없음"
                    Log.d("Mypage", "이름 불러오기: ${userName}")

                    if (!profileImage.isNullOrEmpty()) {
                        loadProfileImage(profileImage) // URL 형식으로 이미지 로드
                    } else {
                        profileImageView.setImageResource(R.drawable.profile) // 기본 이미지
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Mypage", "데이터 가져오기 실패", exception)
            }
    }

//    // 이미지 선택 시 호출되는 메서드
//    override fun onImageSelected(imageUri: Uri) {
//        Glide.with(this)
//            .load(imageUri) // 비트맵 대신 URI 사용
//            .apply(RequestOptions.circleCropTransform()) // 비트맵을 둥글게 처리
//            .placeholder(R.drawable.profile) // 로딩 중에 기본 이미지 표시
//            .error(R.drawable.profile) // 오류 발생 시 기본 이미지 표시
//            .into(profileImageView)
//
//        // 이미지 URI를 Firebase에 업데이트
//        updateUserProfileImage(imageUri.toString())
//    }
    // 이미지 선택 시 호출되는 메서드
    override fun onImageSelected(imageUri: Uri) {
        uploadProfileImage(imageUri) // 이미지를 Firebase Storage에 업로드
    }

    private fun uploadProfileImage(imageUri: Uri) {
        val storageReference = storage.reference
        val userId = sharedPreferences.getString("UNIQUE_CODE", null)

        // 이미지 URI를 Storage에 업로드
        val profileImageRef = storageReference.child("profile_images/$userId.jpg")
        profileImageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // 업로드 완료 후 다운로드 가능한 URL 가져오기
                profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateUserProfileImage(uri.toString()) // URL을 Firestore에 저장
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Mypage", "이미지 업로드 실패", exception)
            }
    }

//    private fun updateUserProfileImage(imageUrl: String) {
//        firestore.collection("users").document(userId!!).update("profileImage", imageUrl)
//            .addOnSuccessListener {
//                Log.d("Mypage", "바뀐 이미지 저장")
//            }
//            .addOnFailureListener { exception ->
//                Log.e("Mypage", "업데이트 실패", exception)
//            }
//    }
    private fun updateUserProfileImage(imageUrl: String) {
        firestore.collection("users").document(userId!!).update("profileImage", imageUrl)
            .addOnSuccessListener {
                Log.d("Mypage", "바뀐 이미지 저장")
                loadProfileImage(imageUrl) // 이미지 업데이트 후 로드
            }
            .addOnFailureListener { exception ->
                Log.e("Mypage", "업데이트 실패", exception)
            }
    }

    private fun loadProfileImage(url: String) {
            // Glide를 사용하여 프로필 이미지 둥글게 로드
            Glide.with(this)
                .load(url)
                .apply(RequestOptions.circleCropTransform()) // 이미지를 둥글게 처리
                .placeholder(R.drawable.profile) // 로딩 중에 기본 이미지 표시
                .error(R.drawable.profile) // 오류 발생 시 기본 이미지 표시
                .into(profileImageView)
    }

    // 이미지 삭제 시 호출되는 메서드
    override fun onImageDeleted() {
        profileImageView.setImageResource(R.drawable.profile) // 기본 이미지로 설정

//        // 기본 이미지 가져오기
//        val defaultBitmap = BitmapFactory.decodeResource(resources, R.drawable.profile)
//
//        // ImageView의 크기와 비율에 맞게 자르기
//        val width = profileImageView.width
//        val height = profileImageView.height
//        val resizedBitmap = Bitmap.createScaledBitmap(defaultBitmap, width, height, true)
//
//        // 비트맵을 ImageView에 설정
//        profileImageView.setImageBitmap(resizedBitmap)
    }

    private fun showLogoutDialog() {
        LogoutFragment(requireContext()).show()
    }

    private fun showDeleteDialog() {
        DeleteFragment(requireContext()).show()
    }

    private fun showRenameDialog() {
        val renameDialog = RenameFragment(requireContext()) { newName ->
            tv_name.text = newName
            updateUserName(newName) // 이름 업데이트
        }
        renameDialog.show()
    }

    private fun updateUserName(newName: String) {
        firestore.collection("users").document(userId!!).update("name", newName)
            .addOnSuccessListener {
                Log.d("Mypage", "바뀐 이름 저장")
            }
            .addOnFailureListener { exception ->
                Log.e("Mypage", "바뀐 이름 저장 실패", exception)
            }
    }
}
