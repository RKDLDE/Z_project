package com.example.z_project.chat.calendar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_project.databinding.DialogAddEventBinding

class DialogAddEvent(private val context: Context, private val calendarClearSelection: () -> Unit) {
    private val dialog = Dialog(context)

    fun show(events: List<ScheduleModel>, onAddEventClick: () -> Unit) {
        val bindingDialog = DialogAddEventBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // RecyclerView 설정
        bindingDialog.addCalendarEventContentRv.layoutManager = LinearLayoutManager(context)
        val adapter = EventRVAdapter(events)
        bindingDialog.addCalendarEventContentRv.adapter = adapter

        // 이벤트가 있을 경우 RecyclerView를 보이게 설정
        if (events.isNotEmpty()) {
            bindingDialog.addCalendarEventContentRv.visibility = View.VISIBLE
            bindingDialog.addCalendarTitle.visibility = View.GONE
        } else {
            bindingDialog.addCalendarEventContentRv.visibility = View.GONE
            bindingDialog.addCalendarTitle.visibility = View.VISIBLE
        }

        // 추가 버튼 클릭 시 showEventDetailsDialog 호출
        bindingDialog.addCalendarIcon.setOnClickListener {
            onAddEventClick() // 일정 작성 다이얼로그 호출
        }

        // 다이얼로그가 닫힐 때 날짜 선택 해제
        dialog.setOnDismissListener {
            calendarClearSelection()
        }

        // 다이얼로그 보여주기
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
