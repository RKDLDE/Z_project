package com.example.z_project.chat.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.databinding.ItemCalendarCategoryBinding
import com.example.z_project.databinding.ItemCategoryColorBinding

class ColorAdapter(
    private val colors: List<ColorEnum>,
    private val usedColors: List<ColorEnum>,
    private val onColorSelected: (ColorEnum) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION // 선택된 색상의 포지션

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemCategoryColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val colorEnum = colors[position]
        holder.bind(colorEnum, position)
    }

    override fun getItemCount(): Int = colors.size

    inner class ColorViewHolder(private val binding: ItemCategoryColorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(colorEnum: ColorEnum, position: Int) {
            binding.colorCircle.background.setTint(ContextCompat.getColor(binding.root.context, colorEnum.color ?: R.color.calendar_color_yellow))

            // 이미 선택된 색상은 영구적인 체크 표시
            if (usedColors.contains(colorEnum)) {
                binding.checkMark.visibility = View.VISIBLE
                binding.root.isEnabled = false // Disable interaction with this item
            } else { // 선택 가능한 색상이면
                binding.checkMark.visibility = if (position == selectedPosition) View.VISIBLE else View.GONE

                // 클릭 리스너 설정
                binding.root.setOnClickListener {
                    notifyItemChanged(selectedPosition) // 이전 선택된 아이템을 갱신
                    selectedPosition = adapterPosition // 현재 아이템을 선택
                    notifyItemChanged(selectedPosition) // 현재 선택된 아이템을 갱신
                    onColorSelected(colorEnum) // 선택된 색상 전달
                }
            }
        }
    }
}
