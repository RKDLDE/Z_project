package com.example.z_project.record

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.z_project.R
import com.example.z_project.databinding.FragmentFinalBinding

class FeedFinalFragment:Fragment() {
    private lateinit var binding: FragmentFinalBinding
    private lateinit var tv_name: TextView
    private lateinit var profileImageView: ImageView
    private var userId: String? = null

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

        // 번들에서 데이터 가져오기
        val uploadEmoji = arguments?.getString("uploadEmoji")
        val feedText = arguments?.getString("feedText")
        val uploadImage = arguments?.getString("uploadImage")
        val userName = arguments?.getString("userName")
        val uploadDate = arguments?.getString("uploadDate")
        val profileImage = arguments?.getString("profileImage")

        Log.d("FeedFinal","uploadEmoji: ${uploadEmoji}")
        Log.d("FeedFinal","feedText: ${feedText}")
        Log.d("FeedFinal","uploadImage: ${uploadImage}")
        Log.d("FeedFinal","userName: ${userName}")
        Log.d("FeedFinal","uploadDate: ${uploadDate}")
        Log.d("FeedFinal","profileImage: ${profileImage}")

        binding.myEmoji.setText(uploadEmoji) // 이모지를 TextView에 설정

        if (!feedText.isNullOrEmpty()) {
            binding.myEditText.visibility = View.VISIBLE // myEditText를 보이게 설정
            binding.myEditText.setText(feedText)
        }


        binding.time.text = feedText
        // 프로필 이미지와 업로드한 이미지를 설정
        binding.nickname.text = userName // 사용자 이름 설정
        binding.time.text = uploadDate // 업로드 시간 설정

        // Glide를 사용하여 이미지 로드
        loadProfileImage(profileImage)
        loadUploadImage(uploadImage) // 업로드한 이미지 로드

        val backButton = view.findViewById<ImageButton>(R.id.ib_back)
        // 이미지 버튼 클릭 이벤트 처리
        backButton.setOnClickListener {
            // 이전 Fragment로 이동
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun loadProfileImage(url: String?) {
        // Glide를 사용하여 프로필 이미지를 로드
        Glide.with(this)
            .load(url)
            .circleCrop() // 이미지를 둥글게 처리
            .placeholder(R.drawable.profile) // 로딩 중 기본 이미지 표시
            .error(R.drawable.profile) // 오류 발생 시 기본 이미지 표시
            .into(binding.ivProfile) // ImageView에 설정
    }

    private fun loadUploadImage(url: String?) {
        // Glide를 사용하여 업로드한 이미지를 로드
        Glide.with(this)
            .load(url)
            .into(binding.photo) // ImageView에 설정
    }
}