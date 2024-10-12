package com.example.z_project.upload

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.z_project.databinding.FragmentFinalBinding

class FinalFragment: Fragment() {
    private lateinit var binding: FragmentFinalBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bundle에서 URI 가져오기
        val imageUriString = arguments?.getString("finalImageUri")
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            // 이미지를 ImageView에 설정하는 메서드 호출
            binding.photo.setImageURI(imageUri)  // ImageView의 ID에 맞게 수정 필요
        }
    }
}