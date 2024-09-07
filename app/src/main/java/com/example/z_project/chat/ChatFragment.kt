package com.example.z_project.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.z_project.chat.calendar.GroupCalendarActivity
import com.example.z_project.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.iconGroupCalendar.setOnClickListener {
            startActivity(Intent(activity, GroupCalendarActivity::class.java))
        }

        return binding.root
    }
}