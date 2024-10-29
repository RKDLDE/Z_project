package com.example.z_project.qna

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.z_project.databinding.DialogQnaInfoBinding
import com.example.z_project.databinding.FragmentLogoutBinding
import com.example.z_project.databinding.FragmentQuestionfeedBinding


class QnaInfoDialog : DialogFragment() {
    private lateinit var binding: DialogQnaInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogQnaInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // 다이얼로그의 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}