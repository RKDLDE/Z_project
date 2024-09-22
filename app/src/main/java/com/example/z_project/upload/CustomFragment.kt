package com.example.z_project.upload

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.z_project.databinding.FragmentCustomBinding


class CustomFragment : Fragment() {
    private var _binding: FragmentCustomBinding? = null
    private val binding get() = _binding!!

    private lateinit var customView: CustomView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CustomView와 ImageView 참조 (ViewBinding 사용)
        customView = binding.customView


        binding.sss.setOnClickListener {
            if (customView.isDrawing) {
                customView.isDrawing = false // 그리기 중지
            } else {
                customView.startDrawing() // 그리기 시작
            }
        }

        // Clear 버튼 클릭 시 clear() 호출
        binding.clearButton.setOnClickListener {
            customView.clear() // clear() 메서드 호출
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//

