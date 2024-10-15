package com.example.z_project.upload

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.z_project.BuildConfig
import com.example.z_project.R
import com.example.z_project.databinding.FragmentFinalBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FinalFragment : Fragment() {
    private lateinit var binding: FragmentFinalBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var tv_name: TextView
    private lateinit var profileImageView: ImageView
    private var userId: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("UNIQUE_CODE", null)

        Log.d("Final", "로컬 userId: $userId")

        tv_name = binding.nickname
        profileImageView = binding.ivProfile

        if (userId != null) {
            fetchUserInfo()
        } else {
            Log.e("Final", "userId가 null입니다.")
        }

        // ViewModel에서 URI를 관찰하여 이미지 표시 및 업로드
        sharedViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            binding.photo.setImageURI(uri)
            if (uri != null) {
                Log.d("Fianl", "이미지 URI: ${uri}")
                val currentTime = getCurrentTime() // 현재 시간
                binding.time.text = currentTime // 현재 시간을 TextView에 설정

                // FileProvider를 통해 content:// URI 얻기
                val imageFile = File(uri.path!!) // 경로를 사용하여 파일 객체 생성
                val imageUri: Uri = FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", imageFile)

                uploadImageToFirebase(imageUri, currentTime) // 이미지 업로드 및 Firestore 저장
            }
        }
    }

    // 현재 시간을 "yyyy-MM-dd HH:mm" 형식으로 반환
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }


    // Firebase Storage에 이미지 업로드 후 Firestore에 이미지 URL, 시간, UNIQUE_CODE 저장
    private fun uploadImageToFirebase(imageUri: Uri, currentTime: String) {
        val userId = sharedPreferences.getString("UNIQUE_CODE", null)
        val timestamp = System.currentTimeMillis() // 현재 시간으로 파일 이름 생성
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${userId}_profile_$timestamp.jpg")

        // 이미지 업로드
        storageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                Log.d("Final", "이미지 업로드 성공: ${taskSnapshot.metadata?.path}")

                // Firestore에 이미지 URL과 시간 저장
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    saveDataToFirestore(downloadUrl.toString(), currentTime)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Final", "이미지 업로드 실패", e)
            }
    }


    // Firestore에 이미지 URL, 현재 시간, UNIQUE_CODE 저장
    private fun saveDataToFirestore(imageUrl: String, currentTime: String) {
        if (userId != null) {
            val userData = hashMapOf(
                "profileImageUrl" to imageUrl,
                "uniqueCode" to userId,
                "uploadTime" to currentTime // 현재 시간 저장
            )

            firestore.collection("images").document(userId!!)
                .set(userData)
                .addOnSuccessListener {
                    Log.d("Final", "이미지 URL, 시간, UNIQUE_CODE Firestore에 저장됨")
                }
                .addOnFailureListener { exception ->
                    Log.e("Final", "데이터 저장 실패", exception)
                }
        }
    }

    // 사용자 정보 가져오기
    private fun fetchUserInfo() {
        firestore.collection("users").document(userId!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.getString("name")
                    val profileImageUrl = document.getString("profileImageUrl")

                    tv_name.text = userName ?: "이름 없음"

                    if (!profileImageUrl.isNullOrEmpty()) {
                        loadProfileImage(profileImageUrl)
                    } else {
                        profileImageView.setImageResource(R.drawable.profile)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Mypage", "데이터 가져오기 실패", exception)
            }
    }

    // 프로필 이미지 로드
    private fun loadProfileImage(url: String) {
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(profileImageView)
    }
}
