package com.example.z_project.chat.calendar

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.example.z_project.databinding.ItemSpinnerYearBinding
import java.util.Calendar

class YearSpinnerAdapter (
    context: Context, @LayoutRes private val resId: Int,
    private val yearList: List<Int>, private val currentYear: String,
) : ArrayAdapter<String>(context, resId, yearList.map { it.toString() }) {

    private var selectedPosition = -1 // 선택된 항목의 위치를 저장할 변수

    // 드롭다운하지 않은 상태의 Spinner 항목의 뷰
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemSpinnerYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        binding.itemSpinnerYearTv.text =
            if (selectedPosition == -1) "${currentYear}년" else "${yearList[selectedPosition]}년"

        Log.d("현재년도", "${currentYear}")
        Log.d("현재selectPosition", "${selectedPosition}")

        return binding.root
    }

    // 드롭다운된 항목들 리스트의 뷰
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemSpinnerYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.itemSpinnerYearTv.text = yearList[position].toString()

        return binding.root
    }

    override fun getCount() = yearList.size

    override fun getItem(position: Int) = yearList[position].toString()

    override fun getItemId(position: Int) = position.toLong()

    // 선택된 항목 설정 메소드
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // 변경사항 적용을 위해 데이터셋을 새로고침
    }
}