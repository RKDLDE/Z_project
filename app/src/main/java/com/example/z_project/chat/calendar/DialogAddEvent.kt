package com.example.z_project.chat.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import com.example.z_project.R
import com.example.z_project.databinding.DialogAddEventBinding
import com.example.z_project.databinding.DialogEventDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import com.google.firebase.firestore.FirebaseFirestore


class DialogAddEvent (private val context: Context, private val calendarClearSelection: () -> Unit){
    private val dialog = BottomSheetDialog(context)
    val bindingDialog = DialogAddEventBinding.inflate(LayoutInflater.from(context))

    private lateinit var dayDecorator: DayViewDecorator
    private lateinit var todayDecorator: DayViewDecorator
    private lateinit var selectedMonthDecorator: DayViewDecorator
    private lateinit var otherMonthDecorator: DayViewDecorator

    private lateinit var startCalendarView: MaterialCalendarView
    private lateinit var endCalendarView: MaterialCalendarView

    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker

    private lateinit var selectedDate: CalendarDay


    @SuppressLint("ClickableViewAccessibility")
    fun show(date: CalendarDay) {
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        startCalendarView = dialog.findViewById(R.id.dialog_start_calendar_view)!!
        endCalendarView = dialog.findViewById(R.id.dialog_end_calendar_view)!!

        startTimePicker = dialog.findViewById(R.id.dialog_calendar_start_timepicker)!!
        endTimePicker = dialog.findViewById(R.id.dialog_calendar_end_timepicker)!!

        // 선택된 날짜 텍스트로 표시
        selectedDate = date
        bindingDialog.dialogEventDetailsDate.text = formatDateWithE(selectedDate)

        // 다이얼로그가 닫힐 때 날짜 선택 해제
        dialog.setOnDismissListener {
            calendarClearSelection()
        }

        initView()

        // 하루종일 토글 선택 시 반응
        bindingDialog.dialogEventDetailsToggleOff.setOnClickListener {
            dateToggle(isSelected = true)
        }
        bindingDialog.dialogEventDetailsToggleOn.setOnClickListener {
            dateToggle(isSelected = false)
        }

        // 시작 날짜 선택
        bindingDialog.dialogEventDetailsStartdateSelect.setOnClickListener{
            if (startCalendarView.visibility == View.VISIBLE) {
                startCalendarView.visibility = View.GONE
            } else {
                startCalendarView.visibility = View.VISIBLE
                startTimePicker.visibility = View.GONE
            }
        }

        // 종료 날짜 선택
        bindingDialog.dialogEventDetailsEnddateSelect.setOnClickListener{
            Log.d("캘린더", "나왔나!")
            if (endCalendarView.visibility == View.VISIBLE) {
                endCalendarView.visibility = View.GONE
            } else {
                endCalendarView.visibility = View.VISIBLE
                endTimePicker.visibility = View.GONE
            }
        }

        initTimePickers()

        // 시작 시간 선택
        bindingDialog.dialogEventDetailsStarttimeSelect.setOnClickListener {
            if (startTimePicker.visibility == View.VISIBLE) {
                startTimePicker.visibility = View.GONE
            } else {
                startTimePicker.visibility = View.VISIBLE
                startCalendarView.visibility = View.GONE
            }
        }

        // 종료 시간 선택
        bindingDialog.dialogEventDetailsEndtimeSelect.setOnClickListener {
            if (endTimePicker.visibility == View.VISIBLE) {
                endTimePicker.visibility = View.GONE
            } else {
                endTimePicker.visibility = View.VISIBLE
                endCalendarView.visibility = View.GONE
            }
        }

        // EditText에 업데이트에 따른 체크 아이콘 반응
        bindingDialog.dialogEventDetailsContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전 처리
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때 호출되는 메서드
                // EditText가 비어 있지 않은 경우
                if (!s.isNullOrEmpty()) {
                    bindingDialog.dialogEventDetailsCheckIconOff.visibility = View.GONE
                    bindingDialog.dialogEventDetailsCheckIconOn.visibility = View.VISIBLE
                } else{
                    bindingDialog.dialogEventDetailsCheckIconOff.visibility = View.VISIBLE
                    bindingDialog.dialogEventDetailsCheckIconOn.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후 처리
            }
        })

        // 체크 버튼 (저장 버튼)
        bindingDialog.dialogEventDetailsCheckIconOn.setOnClickListener {
            // 확인 버튼 클릭 시 수행할 작업
            val title = bindingDialog.dialogEventDetailsContent.text.toString()
            val startDate = bindingDialog.selectStartDate.text.toString()
            val endDate = bindingDialog.selectEndDate.text.toString()
            val startTime = bindingDialog.selectStartTime.text.toString()
            val endTime = bindingDialog.selectEndTime.text.toString()

            // Event 객체 생성
            val event = ScheduleModel(1, 1, title, startDate, endDate, startTime, endTime, Category("중요", ColorEnum.getByColor(R.color.calendar_color_yellow)))

            // Firestore에 저장
            saveEventToFirestore(event)

            dialog.dismiss() // 다이얼로그 닫기
        }

        // 다이얼로그 보여주기
        dialog.show()
    }

    private fun initView() = with(bindingDialog) {
        setupCalendar(startCalendarView, true, selectedDate)
        setupCalendar(endCalendarView, false, selectedDate)
    }

    private fun setupCalendar(calendarView: MaterialCalendarView, isStartDate: Boolean, selectedDate: CalendarDay) {
        with(calendarView) {
            // 캘린더 데코레이터 초기화
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

            // 데코레이터 추가
            addDecorators(dayDecorator, todayDecorator, selectedMonthDecorator, otherMonthDecorator)

            // 월 변경 리스너 설정 & 타이틀 포맷
            setTitleFormatter(CalendarDecorators.koreanMonthTitleFormatter())
            setOnMonthChangedListener { widget, date ->
                widget.clearSelection()
                removeDecorators()
                invalidateDecorators()

                // 월 변경에 따른 데코레이터 업데이트
                selectedMonthDecorator = CalendarDecorators.selectedMonthDecorator(
                    context,
                    date.month
                )
                addDecorators(dayDecorator, todayDecorator, selectedMonthDecorator,
                    CalendarDecorators.otherMonthDecorator(context, date.month))

                // 이벤트 필터링 (해당 Dialog에는 필요 x)
            }
            setOnDateChangedListener { _, date, _ ->
                updateSelectedDate(date, isStartDate) // 선택된 날짜 업데이트
            }

            // 선택 전 날짜 초기화
            bindingDialog.selectStartDate.text = formatDate(selectedDate)
            bindingDialog.selectEndDate.text = formatDate(selectedDate)
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
        // 현재 시간 가져오기
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        Log.d("현재 시간", "${calendar.time}")

        // 시작 시간을 현재 시간으로 설정하고 10분 단위로 맞춤
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // 10분 단위로 설정 (10분을 초과한 경우에는 10분 단위로 올림)
        val roundedMinute = (currentMinute + 9) / 10 * 10 % 60
        val hourAdjustment = if (roundedMinute == 0) 1 else 0
        val adjustedHour = currentHour + hourAdjustment

        startTimePicker.hour = adjustedHour
        startTimePicker.minute = roundedMinute

        // 현재 시간을 기준으로 포맷팅하여 selectStartTime 초기화
        val startamPm = if (adjustedHour < 12) "오전" else "오후"
        val formattedStartHour = if (adjustedHour == 0 || adjustedHour == 12) 12 else adjustedHour % 12
        val formattedStartTime = String.format("%s %d:%02d", startamPm, formattedStartHour, roundedMinute)

        // 시작 시간을 다이얼로그에 표시
        bindingDialog.selectStartTime.text = formattedStartTime
        Log.d("시작하는시간", "${formattedStartTime}")

        // 종료 시간을 시작 시간에서 1시간 더한 값으로 설정
        calendar.set(Calendar.HOUR_OF_DAY, adjustedHour)
        calendar.set(Calendar.MINUTE, roundedMinute)
        calendar.add(Calendar.HOUR_OF_DAY, 1)

        val endHour = calendar.get(Calendar.HOUR_OF_DAY)
        val endMinute = calendar.get(Calendar.MINUTE)

        endTimePicker.hour = endHour
        endTimePicker.minute = endMinute

        val endAmPm = if (endHour < 12) "오전" else "오후"
        val formattedEndHour = if (endHour == 0 || endHour == 12) 12 else endHour % 12
        val formattedEndTime = String.format("%s %d:%02d", endAmPm, formattedEndHour, endMinute)

        Log.d("끝나는시간", "${formattedEndTime}")

        // 종료 시간을 다이얼로그에 표시
        bindingDialog.selectEndTime.text = formattedEndTime

        // 시작 시간 선택 로직
        startTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val amPm = if (hourOfDay < 12) "오전" else "오후"
            val formattedHour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
            val formattedTime = String.format("%s %d:%02d", amPm, formattedHour, minute)

            // 시작 시간 텍스트 업데이트
            bindingDialog.selectStartTime.text = formattedTime

            // 종료 시간을 시작 시간에 1시간 더한 값으로 자동 설정
            val newEndCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
                add(Calendar.HOUR_OF_DAY, 1) // 1시간 더하기
            }

            val newEndHour = newEndCalendar.get(Calendar.HOUR_OF_DAY)
            val newEndMinute = newEndCalendar.get(Calendar.MINUTE)
            endTimePicker.hour = newEndHour
            endTimePicker.minute = newEndMinute

            val newEndAmPm = if (newEndHour < 12) "오전" else "오후"
            val newFormattedEndHour = if (newEndHour == 0 || newEndHour == 12) 12 else newEndHour % 12
            val newFormattedEndTime = String.format("%s %d:%02d", newEndAmPm, newFormattedEndHour, newEndMinute)

            // 종료 시간 텍스트 업데이트
            bindingDialog.selectEndTime.text = newFormattedEndTime
        }

        // 종료 시간 선택 로직
        endTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val amPm = if (hourOfDay < 12) "오전" else "오후"
            val formattedHour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
            val formattedTime = String.format("%s %d:%02d", amPm, formattedHour, minute)

            bindingDialog.selectEndTime.text = formattedTime
        }
    }

    private fun formatDateWithE(date: CalendarDay): String {
        val calendar = Calendar.getInstance().apply {
            set(date.year, date.month - 1, date.day)  // MaterialCalendarView uses 1-based months
        }
        val format = SimpleDateFormat("M월 d일 EEEE", Locale("ko", "KR"))
        return format.format(calendar.time)
    }

    // 하루종일 토글 상태 변경 함수
    private fun dateToggle(isSelected: Boolean){
        if(isSelected){
            bindingDialog.dialogEventDetailsToggleOff.visibility = View.GONE
            bindingDialog.dialogEventDetailsToggleOn.visibility = View.VISIBLE
            bindingDialog.dialogEventDetailsStarttimeSelect.visibility = View.GONE
            bindingDialog.dialogEventDetailsEndtimeSelect.visibility = View.GONE

            bindingDialog.selectStartTime.text = ""
            bindingDialog.selectEndTime.text = ""

        } else{
            bindingDialog.dialogEventDetailsToggleOff.visibility = View.VISIBLE
            bindingDialog.dialogEventDetailsToggleOn.visibility = View.GONE
            bindingDialog.dialogEventDetailsStarttimeSelect.visibility = View.VISIBLE
            bindingDialog.dialogEventDetailsEndtimeSelect.visibility = View.VISIBLE
        }
    }

    // firebase 일정 데이터 업로드
    private fun saveEventToFirestore(event: ScheduleModel) {
        val db = FirebaseFirestore.getInstance()
        val eventsCollection = db.collection("events") // "events"라는 컬렉션에 저장

        eventsCollection.add(event)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Event added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding event", e)
            }
    }
}