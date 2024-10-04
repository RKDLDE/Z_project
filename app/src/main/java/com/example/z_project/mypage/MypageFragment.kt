package com.example.z_project.mypage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.z_project.MainActivity
import com.example.z_project.R
import com.example.z_project.databinding.FragmentMypageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.InputStream
import java.net.URL

class MypageFragment : Fragment(), BottomsheetFragment.ImageSelectionListener { // 인터페이스 구현

    lateinit var binding: FragmentMypageBinding
    private lateinit var tv_name: TextView
    private lateinit var profileImageView: ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // MainActivity로부터 전달된 데이터 가져오기
//        val userName = arguments?.getString("USER_NAME")
//        val profileImageUrl = arguments?.getString("PROFILE_IMAGE")
//        val token = arguments?.getString("TOKEN")
//        // 사용자 이름과 프로필 사진 설정
//        if (userName != null) {
//            tv_name.text = userName
//        }
//
//        // 프로필 이미지 로드
//        if (profileImageUrl != null) {
//            loadProfileImage(profileImageUrl)
//        } else {
//            profileImageView.setImageResource(R.drawable.profile) // 기본 이미지
//        }

        // 친구 관리 버튼 클릭
        binding.llFriend.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, FriendFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        // 버튼 초기화 및 리스너 설정
        tv_name = view.findViewById(R.id.tv_name)
        profileImageView = view.findViewById(R.id.iv_profile)

        val changeProfileButton: ImageButton = view.findViewById(R.id.ib_change_profile)
        changeProfileButton.setOnClickListener {
            // BottomSheetFragment 호출
            val bottomSheetFragment = BottomsheetFragment()
            bottomSheetFragment.setImageSelectionListener(this) // Listener 설정
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }

        val logoutButton: LinearLayout = view.findViewById(R.id.ll_logout)
        logoutButton.setOnClickListener {
            showLogoutDialog()
        }

        val deleteButton: LinearLayout = view.findViewById(R.id.ll_delete)
        deleteButton.setOnClickListener {
            showDeleteDialog()
        }

        val renameButton: ImageButton = view.findViewById(R.id.ib_rename)
        renameButton.setOnClickListener {
            showRenameDialog()
        }
    }

    // 이미지 선택 시 호출되는 메서드
    override fun onImageSelected(imageUri: Uri) {
        // 선택된 이미지의 비트맵 가져오기
        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)

        // ImageView의 크기와 비율에 맞게 자르기
        val width = profileImageView.width
        val height = profileImageView.height

        // 비트맵을 ImageView 크기에 맞게 조정 (중앙에서 잘라냄)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

        // 비트맵을 ImageView에 설정
        profileImageView.setImageBitmap(resizedBitmap)
    }

//    private fun loadProfileImage(url: String) {
//        // 비동기적으로 이미지 로드
//        AsyncTask.execute {
//            try {
//                val inputStream: InputStream = URL(url).openStream()
//                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
//
//                // UI 스레드에서 이미지 설정
//                activity?.runOnUiThread {
//                    profileImageView.setImageBitmap(bitmap)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
    // 이미지 삭제 시 호출되는 메서드
    override fun onImageDeleted() {
        // 기본 이미지 가져오기
        val defaultBitmap = BitmapFactory.decodeResource(resources, R.drawable.profile)

        // ImageView의 크기와 비율에 맞게 자르기
        val width = profileImageView.width
        val height = profileImageView.height
        val resizedBitmap = Bitmap.createScaledBitmap(defaultBitmap, width, height, true)

        // 비트맵을 ImageView에 설정
        profileImageView.setImageBitmap(resizedBitmap)
    }

    private fun showLogoutDialog() {
        LogoutFragment(requireContext()).show()
    }

    private fun showDeleteDialog() {
        DeleteFragment(requireContext()).show()
    }

    private fun showRenameDialog() {
        val renameDialog = RenameFragment(requireContext()) { newName ->
            tv_name.text = newName // 다이얼로그에서 변경된 이름을 TextView에 표시
        }
        renameDialog.show()
    }
}
