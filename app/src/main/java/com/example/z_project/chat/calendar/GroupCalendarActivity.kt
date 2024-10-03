package com.example.z_project.chat.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.z_project.R
import com.example.z_project.chat.calendar.CalendarDecorators.otherMonthDecorator
import com.example.z_project.databinding.ActivityGroupCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale


class GroupCalendarActivity : AppCompatActivity() {

    lateinit var binding: ActivityGroupCalendarBinding

//    private val viewModel: CalendarViewModel by viewModels()
//    private val sharedViewModel: GroupSharedViewModel by viewModels()
//
//    private val scheduleListAdapter: ScheduleListAdapter by lazy {
//        ScheduleListAdapter(
//            onClickItem = { item ->
//                onScheduleItemClick(item)
//            }
//        )
//    }

    private lateinit var dayDecorator: DayViewDecorator
    private lateinit var todayDecorator: DayViewDecorator
    private lateinit var selectedMonthDecorator: DayViewDecorator
    private lateinit var otherMonthDecorator: DayViewDecorator
    private lateinit var sundayDecorator: DayViewDecorator
    private lateinit var saturdayDecorator: DayViewDecorator
    private lateinit var eventDecorator: DayViewDecorator
    private lateinit var customDayViewDecorator: DayViewDecorator
    private val currentYear: String = Calendar.getInstance().get(Calendar.YEAR).toString()

    private var dummyScheduleList = listOf(
        ScheduleModel(
            authId = 1,
            groupId = 1,
            title = "회의",
            startDate = "2024.09.07",
            endDate = "2024.09.07",
            userColor = ColorEnum.getByColor(R.color.calendar_color_pink)
        ),
        ScheduleModel(
            authId = 2,
            groupId = 1,
            title = "개강",
            startDate = "2024.09.02",
            endDate = "2024.09.02",
            userColor = ColorEnum.getByColor(R.color.calendar_color_blue)
        ),
        ScheduleModel(
            authId = 3,
            groupId = 1,
            title = "출근",
            startDate = "2024.09.02",
            endDate = "2024.09.02",
            userColor = ColorEnum.getByColor(R.color.calendar_color_yellow)
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()

        // X 아이콘 클릭 시 (캘린더 나가기)
        binding.groupCalenderExit.setOnClickListener {
            finish()
        }
    }

    private fun initView() = with(binding) {
        //recyclerViewSchedule.adapter = scheduleListAdapter
        val exampleDate = CalendarDay.today()
        with(calendarView) {
            // 데코레이터 초기화
            dayDecorator = CalendarDecorators.dayDecorator(this@GroupCalendarActivity)
            todayDecorator = CalendarDecorators.todayDecorator(this@GroupCalendarActivity)
            sundayDecorator = CalendarDecorators.sundayDecorator(this@GroupCalendarActivity)
            saturdayDecorator = CalendarDecorators.saturdayDecorator(this@GroupCalendarActivity)
            otherMonthDecorator = CalendarDecorators.otherMonthDecorator(
                this@GroupCalendarActivity,
                Calendar.getInstance().get(Calendar.MONTH) + 1)
            selectedMonthDecorator = CalendarDecorators.selectedMonthDecorator(
                this@GroupCalendarActivity,
                Calendar.getInstance().get(Calendar.MONTH) + 1)
            eventDecorator = CalendarDecorators.eventDecorator(
                this@GroupCalendarActivity,
                dummyScheduleList
            )
            //customDayViewDecorator = CalendarDecorators.PaddingDayViewDecorator(this@GroupCalendarActivity, exampleDate)

            // 캘린더뷰에 데코레이터 추가
            addDecorators(
                //customDayViewDecorator,
                dayDecorator,
                todayDecorator,
                selectedMonthDecorator,
                otherMonthDecorator,
                sundayDecorator,
                saturdayDecorator,
                eventDecorator,
            )

            // 월 표시 부분 커스텀
            setTitleFormatter(CalendarDecorators.koreanMonthTitleFormatter())

            // 월 변경 리스너 설정
            setOnMonthChangedListener { widget, date ->
                // 캘린더 위젯에서 현재 선택된 날짜를 모두 선택 해제
                widget.clearSelection()
                // 캘린더 위젯에 적용된 모든 데코레이터를 제거
                removeDecorators()
                // 데코레이터가 제거되고 위젯이 다시 그려지도록
                invalidateDecorators()

                // 새로운 월에 해당하는 데코레이터를 생성하여 selectedMonthDecorator에 할당
                selectedMonthDecorator =
                    CalendarDecorators.selectedMonthDecorator(
                        this@GroupCalendarActivity,
                        date.month
                    )
                // 새로 생성한 데코레이터를 캘린더 위젯에 추가
                addDecorators(
                    //customDayViewDecorator,
                    dayDecorator,
                    todayDecorator,
                    selectedMonthDecorator,
                    otherMonthDecorator(this@GroupCalendarActivity, date.month),
                    sundayDecorator,
                    saturdayDecorator,
                    eventDecorator,
                )

                // 변경 된 일에 해당하는 일정 목록을 필터링하고 업데이트
//                viewModel.filterScheduleListByDate(calendarDayToDate(date))
                // 변경 된 월에 해당하는 일정 목록을 필터링하고 업데이트
//                viewModel.filterDataByMonth(calendarDayToDate(date))
            }

            // 요일 텍스트 포메터 설정
            setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
            // 헤더 텍스트 모양 설정
            setHeaderTextAppearance(R.style.CalendarWidgetHeader)
            // 범위 선택 리스너 설정
            setOnRangeSelectedListener { widget, dates ->
                if (dates.isNotEmpty()) {
                    // 날짜 범위가 선택되었을 때 showEventDetails() 호출
                    showEventDetailsDialog()
                }
            }
            // 날짜 변경 리스너 설정
            setOnDateChangedListener { widget, date, selected ->
//                val filteredEvents = filterEventsByDate(date)
//                EventRVAdapter.submitList(filteredEvents)
                if(selected){
                    showEventDetailsDialog()
                }
            }

            // Year Spinner Adapter 연결
            val years = (2000..2050).toList() // 원하는 년도 범위를 설정
            var isInitialSelected = false // Spinner의 초기값 설정을 위한 플래그

            val yearSpinnerAdapter = YearSpinnerAdapter(this@GroupCalendarActivity, R.layout.item_spinner_year, years, currentYear)
            binding.selectYearSpinner.setAdapter(yearSpinnerAdapter)
            binding.selectYearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (isInitialSelected) {
                        yearSpinnerAdapter.setSelectedPosition(position) // 선택한 항목 설정

                        val selectedYear = years[position]
                        binding.calendarView.currentDate = CalendarDay.from(selectedYear, binding.calendarView.currentDate.month, 1)

                        val value = parent.getItemAtPosition(position).toString()
                        Toast.makeText(this@GroupCalendarActivity, value, Toast.LENGTH_SHORT).show()
                    } else{
                        isInitialSelected = true
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
            // Spinner 초기화 시 currentYear를 기본으로 설정
            val currentYearPosition = years.indexOf(currentYear.toInt())
            binding.selectYearSpinner.setSelection(currentYearPosition)
        }
    }

    private fun initViewModel() {
//        viewModel.apply {
//            lifecycleScope.launch {
//                filteredByDate.collect {
//                    scheduleListAdapter.submitList(it.list)
//                    // 선택 된 날짜의 요일 텍스트 설정
//                    val dayOfWeekString = when (it.date?.get(Calendar.DAY_OF_WEEK)) {
//                        Calendar.MONDAY -> "월"
//                        Calendar.TUESDAY -> "화"
//                        Calendar.WEDNESDAY -> "수"
//                        Calendar.THURSDAY -> "목"
//                        Calendar.FRIDAY -> "금"
//                        Calendar.SATURDAY -> "토"
//                        Calendar.SUNDAY -> "일"
//                        else -> ""
//                    }
//                    binding.tvDate.text = "${it.date?.get(Calendar.MONTH)?.plus(1)}.${it.date?.get(Calendar.DAY_OF_MONTH)}. $dayOfWeekString"
//                }
//            }
//
//            lifecycleScope.launch {
//                uiState.collect { uiState ->
//                }
//            }
//
//            lifecycleScope.launch {
//                filteredByMonth.collect { uiState ->
//                    // 월이 변경 될 때 이벤트 데코레이터 추가
//                    val eventDecorator = CalendarDecorators.eventDecorator(this@GroupCalenderActivity, uiState)
//                    binding.calendarView.addDecorator(eventDecorator)
//                }
//            }
//        }
//
//        sharedViewModel.apply {
//            lifecycleScope.launch {
//                key.collect { it?.let { key -> viewModel.setEntity(key) } }
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup any resources if needed
    }

//    private fun onScheduleItemClick(item: ScheduleModel) {
//        sharedViewModel.setScheduleKey(item.key!!)
//        sharedViewModel.setScheduleEntryType(CalendarEntryType.DETAIL)
//        supportFragmentManager.beginTransaction().apply {
//            setCustomAnimations(
//                R.anim.enter_animation,
//                R.anim.exit_animation,
//                R.anim.enter_animation,
//                R.anim.exit_animation
//            )
//            replace(
//                R.id.fg_activity_group,
//                RegisterScheduleFragment()
//            )
//            addToBackStack(null)
//            commit()
//        }
//    }

    private fun calendarDayToDate(calendarDay: CalendarDay): Calendar {
        return Calendar.getInstance().apply {
            set(calendarDay.year, calendarDay.month - 1, calendarDay.day) // Adjust month index
        }
    }

    private fun showEventDetailsDialog() { //일정 목록 확인 및 추가 버튼
        val selectedDate = binding.calendarView.selectedDate // 또는 적절히 선택된 날짜 가져오기
        val filteredEvents = filterEventsByDate(selectedDate!!)

        Log.d("선택날짜", "${selectedDate}")
        Log.d("스케줄내용", "${filteredEvents}")

        //Dialog 정의
        val dialogEventDetails = DialogEventDetails(this) {
            binding.calendarView.clearSelection() // 날짜 선택 해제
        }

        //Dialog 표시 (더미데이터)
        dialogEventDetails.show(filteredEvents, selectedDate) {
            showAddEventDialog(selectedDate) // 일정 작성 다이얼로그 호출
        }
    }

    private fun showAddEventDialog(selectedDate: CalendarDay) { //일정 작성 dialog
        //Dialog 정의
        val dialogAddEvent = DialogAddEvent(this){
            binding.calendarView.clearSelection() // 날짜 선택 해제
        }

        //Dialog 표시
        dialogAddEvent.show(selectedDate)
    }

    //날짜 확인 및 필터링 함수
    private fun filterEventsByDate(selectedDate: CalendarDay): List<ScheduleModel> {
        // 선택된 날짜를 Calendar로 변환
        val selectedCalendar = Calendar.getInstance().apply {
            set(selectedDate.year, selectedDate.month - 1, selectedDate.day, 0, 0, 0) // 시간 부분을 00:00:00으로 설정
            set(Calendar.MILLISECOND, 0) // 밀리초를 0으로 설정
        }
        val selectedDateTime = selectedCalendar.time

        return dummyScheduleList.filter { schedule ->
            schedule.startDate?.let { startDate ->
                schedule.endDate?.let { endDate ->
                    try {
                        // startDate와 endDate를 Calendar로 변환
                        val startCalendar = Calendar.getInstance().apply {
                            setTime(SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).parse(startDate))
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val endCalendar = Calendar.getInstance().apply {
                            setTime(SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).parse(endDate))
                            set(Calendar.HOUR_OF_DAY, 23)
                            set(Calendar.MINUTE, 59)
                            set(Calendar.SECOND, 59)
                            set(Calendar.MILLISECOND, 999)
                        }

                        // 선택된 날짜가 startDate와 endDate 범위 내에 있는지 확인
                        selectedDateTime in startCalendar.time..endCalendar.time
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                } ?: false
            } ?: false
        }
    }




}