package com.example.z_project.chatting2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.z_project.R
import com.example.z_project.chatting2.group.ChatGroupListFragment
import com.example.z_project.chatting2.personal.ChatPersonalListFragment
import com.example.z_project.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private lateinit var spinner: Spinner
    private lateinit var fragmentContainer: FrameLayout
    private var inviteImageView: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        spinner = binding.spinner
        fragmentContainer = binding.fragmentContainer

        // Spinner 아이템 설정
        val spinnerItems = listOf("개인", "단체")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Spinner 아이템 클릭 리스너
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                when (position) {
                    0 -> { // 개인 선택 시
                        loadPersonalFragment()
                        removeInviteButton()
                    }
                    1 -> { // 단체 선택 시
                        loadGroupFragment()
                        showInviteButton()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return binding.root
    }

    private fun loadPersonalFragment() {
        val personalFragment = ChatPersonalListFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, personalFragment)
            .commit()
    }

    private fun loadGroupFragment() {
        val groupFragment = ChatGroupListFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, groupFragment)
            .commit()
    }

    private fun showInviteButton() {
        if (inviteImageView == null) {
            inviteImageView = ImageView(context).apply {
                setImageResource(R.drawable.ic_baseline_group_add) // 친구 초대 아이콘 설정
                layoutParams = ConstraintLayout.LayoutParams(100, 100).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                    marginEnd = 20 // 여백 설정
                    topMargin = 20
                }
                // ConstraintLayout에 추가
                (view as? ConstraintLayout)?.addView(this)
            }
        }
    }

    private fun removeInviteButton() {
        inviteImageView?.let {
            (view as? ConstraintLayout)?.removeView(it)
            inviteImageView = null
        }
    }
}
