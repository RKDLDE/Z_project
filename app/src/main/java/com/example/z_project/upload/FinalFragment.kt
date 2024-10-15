package com.example.z_project.upload

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.z_project.databinding.FragmentFinalBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        // TextView에 현재 시간 표시
        binding.time.text = getTime()
    }

    // 현재 시간을 "yyyy-MM-dd hh:mm:ss"로 표시하는 메소드
    private fun getTime(): String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
        return dateFormat.format(date)
    }
}