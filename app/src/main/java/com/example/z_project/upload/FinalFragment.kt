package com.example.z_project.upload

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.z_project.databinding.FragmentFinalBinding

class FinalFragment: Fragment() {
    private lateinit var binding: FragmentFinalBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()  // ViewModel 인스턴스

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel에서 URI를 관찰하여 이미지 표시
        sharedViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            binding.photo.setImageURI(uri)  // ImageView의 ID에 맞게 수정 필요
        }
    }
}