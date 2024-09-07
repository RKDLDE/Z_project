package com.example.z_project.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.z_project.databinding.FragmentMypageBinding
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.example.z_project.MainActivity
import com.example.z_project.databinding.FragmentLoginBinding
import com.example.z_project.login.LoginFragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentChatBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.z_project.mypage.BottomsheetFragment
import com.example.z_project.mypage.FriendFragment
import com.google.android.material.dialog.MaterialDialogs
import com.example.z_project.mypage.LogoutFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_project.mypage.DeleteFragment
class MypageFragment : Fragment() {


//    lateinit var binding: FragmentMypageBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentMypageBinding.inflate(inflater, container, false)
//
//        return binding.root
//    }

    //친구관리 버튼 클릭
    lateinit var binding: FragmentMypageBinding
    //private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding=FragmentMypageBinding.inflate(inflater,container,false)
        val view3 = binding.root

        binding.llFriend.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,FriendFragment()).commitAllowingStateLoss()
        }
        return view3
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Fragment의 레이아웃을 설정
//        return inflater.inflate(R.layout.fragment_mypage, container, false)
//    }

    //흠 작은 버튼 관리
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼들을 가져와 이벤트 리스너를 설정
        val button1: ImageButton = view.findViewById(R.id.ib_change_profile)
        button1.setOnClickListener {
            // 버튼 1 클릭 이벤트 처리
            showBottomSheet()
        }

        // 추가적인 버튼 이벤트 처리도 이곳에 작성
        val button2: LinearLayout = view.findViewById(R.id.ll_logout)
        button2.setOnClickListener {
            // 버튼 1 클릭 이벤트 처리
            showLogoutDialog()
        }

        val button3: LinearLayout = view.findViewById(R.id.ll_delete)
        button3.setOnClickListener {
            // 버튼 1 클릭 이벤트 처리
            showDeleteDialog()
        }

//        val button4: LinearLayout = view.findViewById(R.id.ib_back)
//        button4.setOnClickListener {
//            // 버튼 1 클릭 이벤트 처리
//            onAttach(context)
//        }

    }

    private fun showLogoutDialog() {
        LogoutFragment(requireContext()).show()
    }

    private fun showDeleteDialog() {
        DeleteFragment(requireContext()).show()
    }

    //bottonsheet 전용 함수
    private fun showBottomSheet() {
        // BottomSheetDialog 생성
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // BottomSheet 레이아웃 설정
        val view = layoutInflater.inflate(R.layout.fragment_bottomsheet, null)
        bottomSheetDialog.setContentView(view)

        // BottomSheet 표시
        bottomSheetDialog.show()
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                // 뒤로가기 클릭시 동작하는 로직
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .remove(FriendFragment())
//                    .commit()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
//    }

}