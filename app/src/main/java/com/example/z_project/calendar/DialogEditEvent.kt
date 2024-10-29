package com.example.z_project.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import com.example.z_project.R
import com.example.z_project.databinding.DialogEditEventBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class DialogEditEvent (private val context: Context){
    private val dialog = BottomSheetDialog(context)
    val bindingDialog = DialogEditEventBinding.inflate(LayoutInflater.from(context))

    private lateinit var dayDecorator: DayViewDecorator
    private lateinit var todayDecorator: DayViewDecorator
    private lateinit var selectedMonthDecorator: DayViewDecorator
    private lateinit var otherMonthDecorator: DayViewDecorator

    private lateinit var startCalendarView: MaterialCalendarView
    private lateinit var endCalendarView: MaterialCalendarView

    private lateinit var startTimePicker: TimePicker
    private lateinit var endTimePicker: TimePicker

    private lateinit var selectedDate: CalendarDay

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    fun show(selectedEvent: ScheduleModel) {
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // user CODE 가져오기
        sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)

        startCalendarView = dialog.findViewById(R.id.dialog_edit_start_calendar_view)!!
        endCalendarView = dialog.findViewById(R.id.dialog_edit_end_calendar_view)!!

        startTimePicker = dialog.findViewById(R.id.dialog_edit_calendar_start_timepicker)!!
        endTimePicker = dialog.findViewById(R.id.dialog_edit_calendar_end_timepicker)!!

        // 선택한 카테고리와 제목으로 초기화
        bindingDialog.dialogEventEditCategoryName.text = selectedEvent.category.name
        bindingDialog.dialogEventEditContent.setText(selectedEvent.title)

        // 시작 날짜와 종료 날짜 텍스트 설정
        bindingDialog.editStartDate.text = selectedEvent.startDate
        bindingDialog.editEndDate.text = selectedEvent.endDate

        // 시작 시간과 종료 시간 설정
        bindingDialog.editStartTime.text = selectedEvent.startTime
        bindingDialog.editEndTime.text = selectedEvent.endTime


        // selectedEvent의 startDate를 CalendarDay로 변환하여 selectedDate를 초기화
        selectedDate = parseStringToCalendarDay(selectedEvent.startDate!!)
        initView()

        // 토글 초기화
        if(selectedEvent.startTime == ""){
            dateToggle(isSelected = true)
        } else{
            dateToggle(isSelected = false)
            initTimePickers(selectedEvent)
        }

        // 하루종일 토글 선택 시 반응
        bindingDialog.dialogEventEditToggleOff.setOnClickListener {
            dateToggle(isSelected = true)
            showCheckIconOn() // 토글 변경 시 체크 아이콘 업데이트
        }
        bindingDialog.dialogEventEditToggleOn.setOnClickListener {
            dateToggle(isSelected = false)
            initTimePickers(selectedEvent)
            showCheckIconOn() // 토글 변경 시 체크 아이콘 업데이트
        }

        // 시작 날짜 선택
        bindingDialog.dialogEventEditStartdateSelect.setOnClickListener{
            if (startCalendarView.visibility == View.VISIBLE) {
                startCalendarView.visibility = View.GONE
            } else {
                startCalendarView.visibility = View.VISIBLE
                startTimePicker.visibility = View.GONE
            }
        }

        // 종료 날짜 선택
        bindingDialog.dialogEventEditEnddateSelect.setOnClickListener{
            if (endCalendarView.visibility == View.VISIBLE) {
                endCalendarView.visibility = View.GONE
            } else {
                endCalendarView.visibility = View.VISIBLE
                endTimePicker.visibility = View.GONE
            }
        }

        initTimePickers(selectedEvent)

        // 시작 시간 선택
        bindingDialog.dialogEventEditStarttimeSelect.setOnClickListener {
            if (startTimePicker.visibility == View.VISIBLE) {
                startTimePicker.visibility = View.GONE
            } else {
                startTimePicker.visibility = View.VISIBLE
                startCalendarView.visibility = View.GONE
            }
        }

        // 종료 시간 선택
        bindingDialog.dialogEventEditEndtimeSelect.setOnClickListener {
            if (endTimePicker.visibility == View.VISIBLE) {
                endTimePicker.visibility = View.GONE
            } else {
                endTimePicker.visibility = View.VISIBLE
                endCalendarView.visibility = View.GONE
            }
        }

        // EditText에 업데이트에 따른 체크 아이콘 반응
        bindingDialog.dialogEventEditContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전 처리
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때 호출되는 메서드
                // EditText가 비어 있지 않은 경우
//                if (!s.isNullOrEmpty()) {
//                    bindingDialog.dialogEventEditCheckIconOff.visibility = View.GONE
//                    bindingDialog.dialogEventEditCheckIconOn.visibility = View.VISIBLE
//                } else{
//                    bindingDialog.dialogEventEditCheckIconOff.visibility = View.VISIBLE
//                    bindingDialog.dialogEventEditCheckIconOn.visibility = View.GONE
//                }
                if (!s.isNullOrEmpty()) {
                    showCheckIconOn() // 내용이 변경되면 체크 아이콘 업데이트
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후 처리
            }
        })

        // 체크 버튼 (저장 버튼)
        bindingDialog.dialogEventEditCheckIconOn.setOnClickListener {
            // 수정된 데이터 저장
            val updatedEvent = selectedEvent.copy(
                title = bindingDialog.dialogEventEditContent.text.toString(),
                startDate = bindingDialog.editStartDate.text.toString(),
                endDate = bindingDialog.editEndDate.text.toString(),
                startTime = bindingDialog.editStartTime.text.toString(),
                endTime = bindingDialog.editEndTime.text.toString()
            )

            // Firebase에 업데이트
            updateEventInFirestore(updatedEvent)

            dialog.dismiss() // 다이얼로그 닫기
        }

        // 다이얼로그 보여주기
        dialog.show()
    }
    // 체크 아이콘을 보이게 하는 함수
    private fun showCheckIconOn() {
        bindingDialog.dialogEventEditCheckIconOff.visibility = View.GONE
        bindingDialog.dialogEventEditCheckIconOn.visibility = View.VISIBLE
    }


    // String을 CalendarDay로 변환하는 함수
    private fun parseStringToCalendarDay(dateString: String): CalendarDay {
        val dateFormat = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault())
        val date = dateFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid date format")

        val calendar = Calendar.getInstance().apply { time = date }
        return CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
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
                Calendar.getInstance().get(Calendar.MONTH) + 1
            )
            selectedMonthDecorator = CalendarDecorators.selectedMonthDecorator(
                context,
                Calendar.getInstance().get(Calendar.MONTH) + 1
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
                    CalendarDecorators.otherMonthDecorator(context, date.month)
                )

                // 이벤트 필터링 (해당 Dialog에는 필요 x)
            }
            setOnDateChangedListener { _, date, _ ->
                updateSelectedDate(date, isStartDate) // 선택된 날짜 업데이트
            }

            // 선택 전 날짜 초기화
            bindingDialog.editStartDate.text = formatDate(selectedDate)
            bindingDialog.editEndDate.text = formatDate(selectedDate)
        }
    }

    private fun updateSelectedDate(date: CalendarDay, isStartDate: Boolean) {
        val selectedDate = formatDate(date)
        if (isStartDate) {
            bindingDialog.editStartDate.text = selectedDate
        } else {
            bindingDialog.editEndDate.text = selectedDate
        }
        showCheckIconOn()
    }

    private fun formatDate(date: CalendarDay): String {
        val calendar = Calendar.getInstance().apply {
            set(date.year, date.month - 1, date.day)  // MaterialCalendarView uses 1-based months
        }
        val format = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault())
        return format.format(calendar.time)
    }

    // 시간 parse
    private fun parseTime(timeString: String): Pair<Int, Int> {
        val timeParts = timeString.split(" ")
        val amPm = timeParts[0]
        val time = timeParts[1].split(":")
        var hour = time[0].toInt()
        val minute = time[1].toInt()

        // "오전" 또는 "오후"에 따라 12시간제를 24시간제로 변환
        if (amPm == "오후" && hour < 12) {
            hour += 12
        } else if (amPm == "오전" && hour == 12) {
            hour = 0
        }

        return Pair(hour, minute)
    }


    private fun initTimePickers(selectedEvent: ScheduleModel) {
        // startTime과 endTime을 selectedEvent에서 가져와서 TimePicker에 설정
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        val startTimePair: Pair<Int, Int>
        val endTimePair: Pair<Int, Int>

        if(selectedEvent.startTime.isNullOrEmpty() || selectedEvent.endTime.isNullOrEmpty()){
            // 현재 시간을 startTime으로 설정
            startTimePair = Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))

            // endTime은 startTime에 1시간 더한 값으로 설정
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            endTimePair = Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))

            // 다이얼로그에 표시할 초기 시간 설정
            bindingDialog.editStartTime.text = formatTime(startTimePair.first, startTimePair.second)
            bindingDialog.editEndTime.text = formatTime(endTimePair.first, endTimePair.second)
        } else{
            // 기존 startTime과 endTime을 설정
            startTimePair = parseTime(selectedEvent.startTime!!)
            endTimePair = parseTime(selectedEvent.endTime!!)

            // 다이얼로그에 기존 시간을 표시
            bindingDialog.editStartTime.text = selectedEvent.startTime
            bindingDialog.editEndTime.text = selectedEvent.endTime
        }

        startTimePicker.hour = startTimePair.first
        startTimePicker.minute = startTimePair.second

        endTimePicker.hour = endTimePair.first
        endTimePicker.minute = endTimePair.second

        Log.d("시작하는시간", "${selectedEvent.startTime}")
        Log.d("끝나는시간", "${selectedEvent.endTime}")

        // 시작 시간 선택 로직
        startTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val amPm = if (hourOfDay < 12) "오전" else "오후"
            val formattedHour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
            val formattedTime = String.format("%s %d:%02d", amPm, formattedHour, minute)

            // 시작 시간 텍스트 업데이트
            bindingDialog.editStartTime.text = formattedTime

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
            bindingDialog.editEndTime.text = newFormattedEndTime
            showCheckIconOn()
        }

        // 종료 시간 선택 로직
        endTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val amPm = if (hourOfDay < 12) "오전" else "오후"
            val formattedHour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
            val formattedTime = String.format("%s %d:%02d", amPm, formattedHour, minute)

            bindingDialog.editEndTime.text = formattedTime
            showCheckIconOn()
        }
    }

    // 시간 포맷팅 함수
    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val amPm = if (hourOfDay < 12) "오전" else "오후"
        val formattedHour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
        return String.format("%s %d:%02d", amPm, formattedHour, minute)
    }

    // 하루종일 토글 상태 변경 함수
    private fun dateToggle(isSelected: Boolean){
        if(isSelected){
            bindingDialog.dialogEventEditToggleOff.visibility = View.GONE
            bindingDialog.dialogEventEditToggleOn.visibility = View.VISIBLE
            bindingDialog.dialogEventEditStarttimeSelect.visibility = View.GONE
            bindingDialog.dialogEventEditEndtimeSelect.visibility = View.GONE

            bindingDialog.editStartTime.text = ""
            bindingDialog.editEndTime.text = ""

        } else{
            bindingDialog.dialogEventEditToggleOff.visibility = View.VISIBLE
            bindingDialog.dialogEventEditToggleOn.visibility = View.GONE
            bindingDialog.dialogEventEditStarttimeSelect.visibility = View.VISIBLE
            bindingDialog.dialogEventEditEndtimeSelect.visibility = View.VISIBLE
        }
    }

    // ScheduleModel을 Firestore에 업데이트하는 함수
    private fun updateEventInFirestore(event: ScheduleModel) {
        val db = FirebaseFirestore.getInstance()
        val eventsCollection = db.collection("events")

        // documentId가 null이 아니면 업데이트 진행
        event.documentId?.let { id ->
            eventsCollection.document(id).set(event)
                .addOnSuccessListener {
                    Log.d("Firestore", "Event updated with ID: $id")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error updating event", e)
                }
        }
    }
}