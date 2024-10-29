package com.example.z_project.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.databinding.ItemCalendarCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class CalendarCategoryRVAdapter(
    private val categories: ArrayList<Categories>,
    private val onCategorySelected: (Categories) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<CalendarCategoryRVAdapter.ViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // itemview 객체 생성
        val binding: ItemCalendarCategoryBinding = ItemCalendarCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<Categories>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged() // UI 업데이트
    }

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("UNIQUE_CODE", null)
        val category = categories[position]

        if (category.authId == userId) {
            firestore.collection("categories")
                .document(category.categoryId!!)
                .delete()
                .addOnSuccessListener {
                    Log.d("Firestore", "Document successfully deleted!")
                    categories.removeAt(position)
                    notifyItemRemoved(position)
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error deleting document", e)
                    Toast.makeText(context, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "삭제 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
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