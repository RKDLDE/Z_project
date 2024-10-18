package com.example.z_project.chat.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.databinding.ItemCalendarCategoryBinding

class CalendarCategoryRVAdapter(
    private val categories: ArrayList<Categories>,
    private val onCategorySelected: (Categories) -> Unit
) : RecyclerView.Adapter<CalendarCategoryRVAdapter.ViewHolder>() {

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
        fun bind(category: Categories) {
            Log.d("디버깅용", "${category}")

            binding.calendarCategoryName.text = category.name
            Log.d("카테고리 이름", "${category.name}")

            category.color?.let {
                Log.d("카테고리 색깔", it)
                val colorEnum = category.getColorEnum()
                val colorResId = colorEnum!!.color ?: R.color.main_gray // null일 경우 기본 색상 설정
                val color = ContextCompat.getColor(binding.root.context, colorResId)
                binding.calendarCategoryColor.background.setTint(color)
            }

            // 각 아이템 클릭 시 카테고리 결정
            itemView.setOnClickListener {
                onCategorySelected(category)
                selectCategory(position)
            }
        }

        private fun selectCategory(position: Int){
            val selectedCategory = categories[position]
            Log.d("선택된 카테고리", "${selectedCategory.name}, 색상: ${selectedCategory.color}")

            Toast.makeText(context, "선택한 카테고리: ${selectedCategory.name}", Toast.LENGTH_SHORT).show()

        }
    }
}