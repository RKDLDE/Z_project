package com.example.z_project.chat.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.databinding.ItemCalendarEventBinding

class EventRVAdapter (private var events: ArrayList<ScheduleModel>):
    RecyclerView.Adapter<EventRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemCalendarEventBinding = ItemCalendarEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
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

    inner class ViewHolder(val binding: ItemCalendarEventBinding, private val context: Context):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceType")
        fun bind(event: ScheduleModel) {
            binding.calendarEventContent.text = event.title
            Log.d("일정내용", "${event.title}")
            Log.d("유저색상", "userColor: ${event.category.color}, color: ${event.category.color}")
            event.category.color?.let { colorEnum ->
                val colorResId = colorEnum.color ?: R.color.main_gray // null일 경우 기본 색상 설정
                val color = ContextCompat.getColor(binding.root.context, colorResId)
                binding.itemCalendarEvent.background.setTint(color)
            }

            // 아이템 클릭 시 텍스트 입력을 위한 EditText로 변경
            itemView.setOnClickListener {
                startEditing(position)
            }
        }

        private fun startEditing(position: Int) {
            binding.apply {
                calendarEventContent.visibility = View.GONE
                editEventContent.visibility = View.VISIBLE
                editEventContent.requestFocus()

                editEventContent.hint = events[position].title

                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editEventContent, InputMethodManager.SHOW_IMPLICIT)

                // Done 버튼 클릭 시 편집 종료 및 TextView로 전환
                editEventContent.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                        // 편집 종료
                        finishEditing(position, imm)
                        true // 액션 처리 완료
                    } else {
                        false // 액션 처리되지 않음
                    }
                }

                // 포커스가 사라지면 편집 종료
                editEventContent.setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        finishEditing(position, imm)
                    }
                }

//                editEventContent.setOnFocusChangeListener { v, hasFocus ->
//                    if (!hasFocus) {
//                        val newTitle = (v as EditText).text.toString()
//                        events[position].title = newTitle
//                        notifyItemChanged(position)
//                        calendarEventContent.visibility = View.VISIBLE
//                        editEventContent.visibility = View.GONE
//                        imm.hideSoftInputFromWindow(v.windowToken, 0)
//                    }
//                }
            }
        }
        private fun finishEditing(position: Int, imm: InputMethodManager) {
            binding.apply {
                val newTitle = editEventContent.text.toString()
                events[position].title = newTitle.ifEmpty { events[position].title }  // 빈 문자열일 경우 기존 제목 유지
                notifyItemChanged(position)

                // EditText 숨기고 TextView 보여주기
                calendarEventContent.visibility = View.VISIBLE
                editEventContent.visibility = View.GONE

                // 키보드 숨기기
                imm.hideSoftInputFromWindow(editEventContent.windowToken, 0)
            }
        }
    }
}