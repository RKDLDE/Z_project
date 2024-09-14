package com.example.z_project.mypage

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentBottomsheetBinding
import com.example.z_project.databinding.FragmentLoginBinding
import com.example.z_project.databinding.FragmentMypageBinding

class BottomsheetFragment : Fragment() {
    lateinit var binding: FragmentBottomsheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

//    private fun initViews() = with(binding) {
//        select.setOnClickListener {
//            when {
//                // 갤러리 접근 권한이 있는 경우
//                ContextCompat.checkSelfPermission(
//                    this,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_GRANTED
//                -> {
//                    navigateGallery()
//                }
//
//                // 갤러리 접근 권한이 없는 경우 & 교육용 팝업을 보여줘야 하는 경우
//                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                -> {
//                    showPermissionContextPopup()
//                }
//
//                // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
//                else -> requestPermissions(
//                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                    1000
//                )
//            }
//
//        }
//
//        delete.setOnClickListener {
//
//        }
//    }
//
//    private fun navigateGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        // 가져올 컨텐츠들 중에서 Image 만을 가져온다.
//        intent.type = "image/*"
//        // 갤러리에서 이미지를 선택한 후, 프로필 이미지뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
//        startActivityForResult(intent, 2000)
//    }
//
//    private fun showPermissionContextPopup() {
//        AlertDialog.Builder(this)
//            .setTitle("권한이 필요합니다.")
//            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
//            .setPositiveButton("동의하기") { _, _ ->
//                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
//            }
//            .setNegativeButton("취소하기") { _, _ -> }
//            .create()
//            .show()
//    }
}

