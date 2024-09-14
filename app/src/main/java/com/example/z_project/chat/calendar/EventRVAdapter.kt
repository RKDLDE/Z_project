package com.example.z_project.chat.calendar

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.databinding.ItemCalendarEventBinding

class EventRVAdapter (private var events: ArrayList<ScheduleModel>): RecyclerView.Adapter<EventRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemCalendarEventBinding = ItemCalendarEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position])
    }

    fun submitList(newEvents: List<ScheduleModel>) {
        events = newEvents as ArrayList<ScheduleModel>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = events.size

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        events.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(val binding: ItemCalendarEventBinding): RecyclerView.ViewHolder(binding.root) {
        val deleteButton: View = itemView.findViewById(R.id.erase_item_view)

        @SuppressLint("ResourceType")
        fun bind(event: ScheduleModel) {
            binding.calendarEventContent.text = event.title
            Log.d("일정내용", "${event.title}")
            Log.d("유저색상", "userColor: ${event.userColor}, color: ${event.userColor?.color}")
            event.userColor?.color?.let {
                binding.itemCalendarEvent.background.setTint(ContextCompat.getColor(binding.root.context, event.userColor.color))
            }

            // 삭제 텍스트뷰 클릭시 토스트 표시
            deleteButton.setOnClickListener {
                removeData(this.layoutPosition)
                Toast.makeText(binding.root.context, "일정이 삭제됐습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}