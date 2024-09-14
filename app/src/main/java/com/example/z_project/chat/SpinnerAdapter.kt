package com.example.z_project.chat

import android.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.example.z_project.databinding.ItemChatOptionBinding
import com.example.z_project.databinding.ItemSpinnerYearBinding

class chatSpinnerAdapter (
    context: Context,
    private val optionList: List<String>
) : ArrayAdapter<String>(context, 0, optionList) {

    private var selectedPosition = -1 // 선택된 항목의 위치를 저장할 변수


    // 드롭다운하지 않은 상태의 Spinner 항목의 뷰
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemChatOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.itemChatOptionTv.text = optionList[selectedPosition]

        return binding.root
    }

    // 드롭다운된 항목들 리스트의 뷰
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemChatOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.itemChatOptionTv.text = optionList[position].toString()

        return binding.root
    }

    override fun getCount() = optionList.size

    override fun getItem(position: Int) = optionList[position]

    override fun getItemId(position: Int) = position.toLong()

    // 선택된 항목 설정 메소드
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // 변경사항 적용을 위해 데이터셋을 새로고침
    }
}