package com.example.z_project.chat.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.databinding.ItemCalendarCategoryBinding

class CalendarCategoryRVAdapter(private val categories: MutableList<Category>) :
    RecyclerView.Adapter<CalendarCategoryRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarCategoryRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemCalendarCategoryBinding = ItemCalendarCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        categories.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(val binding: ItemCalendarCategoryBinding, private val context: Context):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceType")
        fun bind(categories: Category) {
            binding.calendarCategoryName.text = categories.name
            categories.color.let {
                binding.calendarCategoryColor.background.setTint(ContextCompat.getColor(binding.root.context, categories.color!!.color!!))
            }

            // 아이템 클릭 시 텍스트 입력을 위한 EditText로 변경
            itemView.setOnClickListener {

            }
        }
    }
}