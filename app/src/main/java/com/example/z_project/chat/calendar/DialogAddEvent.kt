package com.example.z_project.chat.calendar

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_project.databinding.DialogAddEventBinding

class DialogAddEvent(private val context: Context, private val calendarClearSelection: () -> Unit) {
    private val dialog = Dialog(context)

    @SuppressLint("ClickableViewAccessibility")
    fun show(events: List<ScheduleModel>, onAddEventClick: () -> Unit) {
        val bindingDialog = DialogAddEventBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // RecyclerView 설정 & Adapter 연결
        bindingDialog.addCalendarEventContentRv.layoutManager = LinearLayoutManager(context)
        val eventAdapter = EventRVAdapter(events as ArrayList<ScheduleModel>)
        bindingDialog.addCalendarEventContentRv.adapter = eventAdapter

        // 리사이클러뷰에 스와이프 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(bindingDialog.addCalendarEventContentRv, eventAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            val widthPixels = context.resources.displayMetrics.widthPixels
            setClamp(widthPixels.toFloat() / 7)    // 1080 / 7 = 154
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(bindingDialog.addCalendarEventContentRv)

        // 클릭 리스너 설정
        swipeHelperCallback.attachClickListener()

        // 다른 곳 터치 시 기존 선택했던 삭제 swipe 초기화
        bindingDialog.addCalendarDialog.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 필요하면 터치 시작 시 동작을 처리할 수 있음 (예: UI 효과)
                }
                MotionEvent.ACTION_UP -> {
                    // 터치가 끝나면 스와이프 상태 초기화 및 performClick 호출
                    swipeHelperCallback.removePreviousClamp() // 스와이프 상태 초기화

                    // 키보드 숨기기
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)

                    view.performClick()  // performClick을 호출하여 클릭 이벤트를 처리하도록 보장
                }
            }
            false  // onTouchListener에서 이벤트를 처리했음을 명시
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
