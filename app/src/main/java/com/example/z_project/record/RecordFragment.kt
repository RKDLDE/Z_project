package com.example.z_project.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.z_project.chat.calendar.GroupCalendarActivity
import com.example.z_project.databinding.FragmentRecordBinding

class RecordFragment : Fragment() {
    lateinit var binding: FragmentRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)

        binding.iconCalendar.setOnClickListener {
            val intent = Intent(context, GroupCalendarActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}