package com.example.z_project.chat.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.databinding.ItemCalendarEventBinding

class EventRVAdapter (private var event: List<ScheduleModel>): RecyclerView.Adapter<EventRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemCalendarEventBinding = ItemCalendarEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(event[position])
    }

    fun submitList(newEvents: List<ScheduleModel>) {
        event = newEvents
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = event.size

    inner class ViewHolder(val binding: ItemCalendarEventBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ScheduleModel) {
            binding.calendarEventContent.text = event.title
            event.calendarColor?.color?.let {
                binding.itemCalendarEvent.setBackgroundResource(it)
            }
        }
    }
}