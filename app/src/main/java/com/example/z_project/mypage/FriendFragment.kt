package com.example.z_project.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.databinding.FragmentFriendBinding

class FriendFragment : Fragment() {
    lateinit var binding: FragmentFriendBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container, false)

        val testDataSet: MutableList<String> = ArrayList()
        for (i in 0..9) {
            testDataSet.add("친구$i")
        }

        val recyclerView: RecyclerView = binding.rcvFriendList
        recyclerView.layoutManager = LinearLayoutManager(context)

        val customAdapter = CustomAdapter(testDataSet)
        recyclerView.adapter = customAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = view.findViewById<ImageButton>(R.id.ib_back)
        // 이미지 버튼 클릭 이벤트 처리
        backButton.setOnClickListener {
            // 이전 Fragment로 이동
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}

