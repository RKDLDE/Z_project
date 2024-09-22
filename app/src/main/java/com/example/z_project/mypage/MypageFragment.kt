package com.example.z_project.mypage

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.MainActivity
import com.example.z_project.R
import com.example.z_project.databinding.FragmentMypageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.z_project.mypage.RenameFragment

class MypageFragment : Fragment() {

    //친구관리 버튼 클릭
    lateinit var binding: FragmentMypageBinding
    private lateinit var tv_name: TextView
    private lateinit var profileImageView: ImageView
    //private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentMypageBinding.inflate(inflater,container,false)
        val view3 = binding.root

        binding.llFriend.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,FriendFragment()).addToBackStack(null).commitAllowingStateLoss()
        }

        return view3
    }


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

        tv_name = view.findViewById(R.id.tv_name)
        val button4: ImageButton = view.findViewById(R.id.ib_rename)
        button4.setOnClickListener {
            // 버튼 1 클릭 이벤트 처리
            showRenameDialog()
        }

        // 프로필 이미지 및 변경 버튼 참조
        profileImageView = view.findViewById(R.id.iv_profile)
        val changeProfileButton = view.findViewById<ImageButton>(R.id.ib_change_profile)

        // 프로필 변경 버튼 클릭 시 BottomSheetDialogFragment 호출
        changeProfileButton.setOnClickListener {
            val bottomSheetFragment = BottomsheetFragment()
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
        }

    }

    private fun showLogoutDialog() {
        LogoutFragment(requireContext()).show()
    }

    private fun showDeleteDialog() {
        DeleteFragment(requireContext()).show()
    }



    private fun showRenameDialog() {
        val renameDialog = RenameFragment(requireContext()) { newName ->
            // 다이얼로그에서 변경된 이름을 TextView에 표시
            tv_name.text = newName
        }
        renameDialog.show()
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

    // BottomSheet에서 선택된 이미지 처리
    fun onImageSelected(imageUri: Uri) {
        profileImageView.setImageURI(imageUri)
    }
    interface ImageSelectionListener {
        fun onImageSelected(imageUri: Uri)
    }


}