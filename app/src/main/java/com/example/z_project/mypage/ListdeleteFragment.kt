package com.example.z_project.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentBottomsheetBinding
import com.example.z_project.databinding.FragmentLoginBinding
import com.example.z_project.databinding.FragmentMypageBinding
import com.google.android.material.dialog.MaterialDialogs
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.z_project.databinding.FragmentListDeleteBinding
import com.example.z_project.databinding.FragmentLogoutBinding

class ListdeleteFragment(
    context: Context,
    private val deleteListener: OnDeleteListener,
    private val position: Int
) : Dialog(context) {

    private lateinit var binding: FragmentListDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // noButton 클릭 시 다이얼로그 닫기
        noButton.setOnClickListener {
            dismiss()
        }

        // yesButton 클릭 시 아이템 삭제 콜백 호출 후 다이얼로그 닫기
        yesButton.setOnClickListener {
            deleteListener.onDelete(position)
            dismiss()
        }
    }
}
