package com.example.z_project.upload

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.z_project.BuildConfig
import com.example.z_project.R
import com.example.z_project.databinding.FragmentFinalBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import com.google.firebase.Timestamp
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

    private var imageUri: Uri? = null // imageUri를 클래스 수준에서 선언

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 번들에서 이모티콘 데이터 받기
        val emoji = arguments?.getString("emoji")
        Log.d("Final", "받은 이모티콘: $emoji")

        sharedPreferences = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("UNIQUE_CODE", null)

        Log.d("Final", "로컬 userId: $userId")

        tv_name = binding.nickname
        profileImageView = binding.ivProfile

        val currentTime = getCurrentTime() // 현재 시간

        // ViewModel에서 URI를 관찰하여 이미지 표시
        sharedViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            binding.photo.setImageURI(uri)
            if (uri != null) {
                Log.d("Final", "이미지 URI: ${uri}")

                // FileProvider를 통해 content:// URI 얻기
                val imageFile = File(uri.path!!) // 경로를 사용하여 파일 객체 생성
                imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    "${BuildConfig.APPLICATION_ID}.provider",
                    imageFile
                )
            }
        }


//        // ViewModel에서 텍스트 관찰
//        sharedViewModel.inputText.observe(viewLifecycleOwner) { inputText ->
//            Log.e("Final", "받은 텍스트: ${inputText!!?: ""}")
//            saveDataToFirestoreWithoutImage(currentTime, emoji, inputText?.takeIf { it.isNotEmpty() } ?: "") // 텍스트 및 데이터 업로드
//        }
        // ViewModel에서 텍스트 관찰
        sharedViewModel.inputText.observe(viewLifecycleOwner) { inputText ->
            Log.e("Final", "받은 텍스트: ${inputText ?: ""}")

            // 텍스트가 null이거나 빈 문자열이면 빈 문자열로 저장
            val textToSave = inputText?.takeIf { it.isNotEmpty() } ?: ""

            // Firebase에 데이터 저장
            saveDataToFirestoreWithoutImage(currentTime, emoji, textToSave)
        }

        // 초기 텍스트 저장 - 관찰자가 호출되지 않을 때
        if (sharedViewModel.inputText.value.isNullOrEmpty()) {
            saveDataToFirestoreWithoutImage(currentTime, emoji, "")
        }

        if (userId != null) {
            fetchUserInfo()
            fetchLatestUserData()
        } else {
            Log.e("Final", "userId가 null입니다.")
        }

        val backButton = view.findViewById<ImageButton>(R.id.ib_back)
        // 이미지 버튼 클릭 이벤트 처리
        val uploadFragment = UploadFragment()
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, uploadFragment)
                .addToBackStack("UploadFragmentTag") // 태그 설정
                .commit()}
    }

    // 현재 시간을 "yyyy-MM-dd HH:mm" 형식으로 반환
    private fun getCurrentTime(): Timestamp {
        return Timestamp(Date()) // 현재 시간을 Firebase Timestamp 형식으로 반환
    }

    // Firestore에 텍스트, 현재 시간, UNIQUE_CODE 먼저 저장
    private fun saveDataToFirestoreWithoutImage(currentTime: Timestamp, emoji: String?, inputText: String?) {
        if (userId != null) {
            val userData = hashMapOf(
                "uniqueCode" to userId,
                "uploadTime" to currentTime,
                "emoji" to (emoji?.ifEmpty { "" } ?: ""),// 이모티콘 추가
                "inputText" to (inputText?.ifEmpty { "" } ?: "") // 텍스트 추가 (입력 값이 없으면 빈 문자열)
            )

            // Firestore의 "images" 컬렉션에 새로운 문서 추가
            firestore.collection("images")
                .add(userData) // set 대신 add를 사용하여 고유 문서 생성
                .addOnSuccessListener { documentReference ->
                    Log.d("Final", "Firestore에 저장됨, 문서 ID: ${documentReference.id}")

                    // 이미지 업로드 후 URL을 해당 문서에 추가
                    if (imageUri != null) {
                        uploadImageAndSaveUrl(documentReference.id) // 이미지 업로드 및 URL 업데이트
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Final", "데이터 저장 실패", exception)
                }
        }
    }

    // Firebase Storage에 이미지 업로드 후 Firestore에 URL 업데이트
    private fun uploadImageAndSaveUrl(documentId: String) {
        val userId = sharedPreferences.getString("UNIQUE_CODE", null)
        val timestamp = System.currentTimeMillis() // 현재 시간으로 파일 이름 생성
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${userId}_profile_$timestamp.jpg")

        // 이미지 업로드
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.d("Final", "이미지 업로드 성공: ${taskSnapshot.metadata?.path}")

                // Firestore에 이미지 URL 업데이트
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    firestore.collection("images").document(documentId)
                        .update("UploadImageUrl", downloadUrl.toString()) // 문서 업데이트
                        .addOnSuccessListener {
                            Log.d("Final", "이미지 URL 업데이트 성공")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Final", "이미지 URL 업데이트 실패", exception)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Final", "이미지 업로드 실패", e)
            }
    }

    // 사용자 정보 가져오기
    private fun fetchUserInfo() {
        firestore.collection("users").document(userId!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.getString("name")
                    val profileImage = document.getString("profileImage")

                    tv_name.text = userName ?: "이름 없음"

                    if (!profileImage.isNullOrEmpty()) {
                        loadProfileImage(profileImage)
                    } else {
                        profileImageView.setImageResource(R.drawable.profile)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Mypage", "데이터 가져오기 실패", exception)
            }
    }

    // 최신 사용자 데이터를 가져오는 메소드
    private fun fetchLatestUserData() {
        Log.d("Final", "사용자 ID: $userId")
        firestore.collection("images")
            .whereEqualTo("uniqueCode", userId!!)
            .orderBy("uploadTime", com.google.firebase.firestore.Query.Direction.DESCENDING) // 최신 순으로 정렬
            .limit(1) // 최신 데이터 한 개만 가져오기
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Final", "문서 개수: ${documents.size()}")
                if (!documents.isEmpty) {
                    val latestDocument = documents.first()
                    val uploadTime = latestDocument.getTimestamp("uploadTime") // 시간 가져오기

                    binding.time.text = formatTimestampToString(uploadTime!!)

                    val text = latestDocument.getString("inputText")
                    if (!text.isNullOrEmpty()) {
                        binding.myEditText.visibility = View.VISIBLE // myEditText를 보이게 설정
                        binding.myEditText.setText(text)
                    }

                    binding.myEmoji.setText(latestDocument.getString("emoji")) // 이모티콘 가져오기
                    Log.d("Final", "최신 시간:  ${latestDocument.getTimestamp("uploadTime")}")
                } else {
                    Log.d("Final", "사용자의 데이터가 없습니다.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Final", "최신 데이터 가져오기 실패", exception)
            }
    }

    // Timestamp를 문자열로 변환하는 함수
    private fun formatTimestampToString(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(timestamp.toDate()) // Timestamp를 Date로 변환 후 문자열로 변환
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
