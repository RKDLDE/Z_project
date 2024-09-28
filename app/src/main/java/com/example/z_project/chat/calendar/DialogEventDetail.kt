package com.example.z_project.chat.calendar

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TimePicker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_project.R
import com.example.z_project.databinding.DialogEventDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintLayout


class DialogEventDetail (private val context: Context, private val calendarClearSelection: () -> Unit){
    private val dialog = BottomSheetDialog(context)
    val bindingDialog = DialogEventDetailsBinding.inflate(LayoutInflater.from(context))

    private lateinit var dayDecorator: DayViewDecorator
    private lateinit var todayDecorator: DayViewDecorator
    private lateinit var selectedMonthDecorator: DayViewDecorator
    private lateinit var otherMonthDecorator: DayViewDecorator

    private lateinit var startCalendarView: MaterialCalendarView
    private lateinit var endCalendarView: MaterialCalendarView

    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker


    @SuppressLint("ClickableViewAccessibility")
    fun show() {
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        startCalendarView = dialog.findViewById(R.id.dialog_start_calendar_view)!!
        endCalendarView = dialog.findViewById(R.id.dialog_end_calendar_view)!!

        startTimePicker = dialog.findViewById(R.id.dialog_calendar_start_timepicker)!!
        endTimePicker = dialog.findViewById(R.id.dialog_calendar_end_timepicker)!!

        // 다이얼로그가 닫힐 때 날짜 선택 해제
        dialog.setOnDismissListener {
            calendarClearSelection()
        }

        initView()

        // 시작 날짜 선택
        bindingDialog.dialogEventDetailsStartdateSelect.setOnClickListener{
            if (startCalendarView.visibility == View.VISIBLE) {
                startCalendarView.visibility = View.GONE
            } else {
                startCalendarView.visibility = View.VISIBLE
            }
        }

        // 종료 날짜 선택
        bindingDialog.dialogEventDetailsEnddateSelect.setOnClickListener{
            Log.d("캘린더", "나왔나!")
            if (endCalendarView.visibility == View.VISIBLE) {
                endCalendarView.visibility = View.GONE
            } else {
                endCalendarView.visibility = View.VISIBLE
            }
        }

        initTimePickers()

        // 시작 시간 선택
        bindingDialog.dialogEventDetailsStarttimeSelect.setOnClickListener {
            if (startTimePicker.visibility == View.VISIBLE) {
                startTimePicker.visibility = View.GONE
            } else {
                startTimePicker.visibility = View.VISIBLE
            }
        }

        // 종료 시간 선택
        bindingDialog.dialogEventDetailsEndtimeSelect.setOnClickListener {
            if (endTimePicker.visibility == View.VISIBLE) {
                endTimePicker.visibility = View.GONE
            } else {
                endTimePicker.visibility = View.VISIBLE
            }
        }

        // 체크 버튼
        bindingDialog.dialogEventDetailsCheckIcon.setOnClickListener {
            // 확인 버튼 클릭 시 수행할 작업
            dialog.dismiss() // 다이얼로그 닫기
        }

        // 다이얼로그 보여주기
        dialog.show()
    }

    private fun toggleVisibilityWithConstraint(view: View, anchorViewId: Int) {
        val constraintLayout = bindingDialog.root as ConstraintLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
            // Remove the constraint if the view is hidden
            constraintSet.clear(view.id, ConstraintSet.TOP)
        } else {
            view.visibility = View.VISIBLE
            // Chain view's top to the bottom of the anchor view
            constraintSet.connect(view.id, ConstraintSet.TOP, anchorViewId, ConstraintSet.BOTTOM, 8) // 8 is the margin
        }

        // Apply the updated constraints
        constraintSet.applyTo(constraintLayout)
    }

    private fun initView() = with(bindingDialog) {
        setupCalendar(startCalendarView, true)
        setupCalendar(endCalendarView, false)
    }

    private fun setupCalendar(calendarView: MaterialCalendarView, isStartDate: Boolean) {
        with(calendarView) {
            // Initialize decorators here, similar to initView
            dayDecorator = CalendarDecorators.dayDecorator(context)
            todayDecorator = CalendarDecorators.todayDecorator(context)
            otherMonthDecorator = CalendarDecorators.otherMonthDecorator(
                context,
                java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1
            )
            selectedMonthDecorator = CalendarDecorators.selectedMonthDecorator(
                context,
                java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1
            )

            // Add decorators
            addDecorators(dayDecorator, todayDecorator, selectedMonthDecorator, otherMonthDecorator)

            // Set title formatter and month changed listener
            setTitleFormatter(CalendarDecorators.koreanMonthTitleFormatter())
            setOnMonthChangedListener { widget, date ->
                widget.clearSelection()
                removeDecorators()
                invalidateDecorators()

                // Update selectedMonthDecorator
                selectedMonthDecorator = CalendarDecorators.selectedMonthDecorator(
                    context,
                    date.month
                )
                addDecorators(dayDecorator, todayDecorator, selectedMonthDecorator,
                    CalendarDecorators.otherMonthDecorator(context, date.month))

                // Filter events or perform other actions as necessary
            }
            setOnDateChangedListener { _, date, _ ->
                updateSelectedDate(date, isStartDate)
            }
        }
    }

    private fun updateSelectedDate(date: CalendarDay, isStartDate: Boolean) {
        val selectedDate = formatDate(date)
        if (isStartDate) {
            bindingDialog.selectStartDate.text = selectedDate
        } else {
            bindingDialog.selectEndDate.text = selectedDate
        }
    }

    private fun formatDate(date: CalendarDay): String {
        val calendar = Calendar.getInstance().apply {
            set(date.year, date.month - 1, date.day)  // MaterialCalendarView uses 1-based months
        }
        val format = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault())
        return format.format(calendar.time)
    }

    private fun initTimePickers() {
        // 시작 시간 선택 로직
        startTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val amPm = if (hourOfDay < 12) "오전" else "오후"
            val formattedHour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
            val formattedTime = String.format("%s %d:%02d", amPm, formattedHour, minute)

            bindingDialog.selectStartTime.text = formattedTime
        }

        // 종료 시간 선택 로직
        endTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val amPm = if (hourOfDay < 12) "오전" else "오후"
            val formattedHour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
            val formattedTime = String.format("%s %d:%02d", amPm, formattedHour, minute)

            bindingDialog.selectEndTime.text = formattedTime
        }
    }

    private fun showAddCalendarDialog() {
        // Logic to show the dialog for adding events
    }
}