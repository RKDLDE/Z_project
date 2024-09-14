package com.example.z_project.chat.calendar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_project.databinding.DialogAddEventBinding

class DialogAddEvent(private val context: Context, private val calendarClearSelection: () -> Unit) {
    private val dialog = Dialog(context)

    fun show(events: List<ScheduleModel>, onAddEventClick: () -> Unit) {
        val bindingDialog = DialogAddEventBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // RecyclerView 설정 & Adapter 연결
        bindingDialog.addCalendarEventContentRv.layoutManager = LinearLayoutManager(context)
        val eventAdapter = EventRVAdapter(events as ArrayList<ScheduleModel>)
        bindingDialog.addCalendarEventContentRv.adapter = eventAdapter

        // 리사이클러뷰에 스와이프 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(eventAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            val widthPixels = context.resources.displayMetrics.widthPixels
            setClamp(widthPixels.toFloat() / 7)    // 1080 / 5 = 216
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(bindingDialog.addCalendarEventContentRv)

        // 다른 곳 터치 시 기존 선택했던 삭제 뷰 닫기
        bindingDialog.addCalendarDialog.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(bindingDialog.addCalendarEventContentRv)
            false
        }

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
