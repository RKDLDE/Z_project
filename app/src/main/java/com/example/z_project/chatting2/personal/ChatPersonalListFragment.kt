package com.example.z_project.chatting2.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_project.chatting2.GroupChat
import com.example.z_project.chatting2.PersonalChat
import com.example.z_project.databinding.FragmentChatPersonalListBinding

class ChatPersonalListFragment : Fragment(){
    lateinit var binding: FragmentChatPersonalListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatPersonalListBinding.inflate(inflater, container, false)

        var dummyList = listOf(
            PersonalChat(
                profile = "",
                name = "도금",
                content = "안뇽",
                alarmCount = 1,
            ),
            PersonalChat(
                profile = "",
                name = "도리",
                content = "머해",
                alarmCount = 2,
            ),
            PersonalChat(
                profile = "",
                name = "냠",
                content = "ㅋㅋㅋ",
                alarmCount = 3,
            ),
        )

        val personalListAdapter = ChatPersonalListAdapter(dummyList)
        binding.personalChatListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.personalChatListRv.adapter = personalListAdapter

        return binding.root
    }
}