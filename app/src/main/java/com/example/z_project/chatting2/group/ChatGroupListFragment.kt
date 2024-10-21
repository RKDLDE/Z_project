package com.example.z_project.chatting2.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.z_project.chatting2.GroupChat
import com.example.z_project.databinding.FragmentChatGroupListBinding
import com.example.z_project.record.RecordFeedRVAdapter

class ChatGroupListFragment : Fragment(){
    lateinit var binding: FragmentChatGroupListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatGroupListBinding.inflate(inflater, container, false)

        var dummyList = listOf(
            GroupChat(
                profile = "",
                name = "JSND",
                alarmCount = 1,
            ),
            GroupChat(
                profile = "",
                name = "그룹채팅방 1",
                alarmCount = 2,
            ),
            GroupChat(
                profile = "",
                name = "그룹채팅방 2",
                alarmCount = 3,
            ),
        )

        // RecyclerView 설정 & Adapter 연결
        binding.groupChatListRv.layoutManager = GridLayoutManager(context, 2)
        val groupListAdapter = ChatGroupListAdapter(dummyList)
        binding.groupChatListRv.adapter = groupListAdapter

        return binding.root
    }
}