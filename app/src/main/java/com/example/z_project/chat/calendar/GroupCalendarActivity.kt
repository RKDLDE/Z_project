package com.example.z_project.chat.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.z_project.R
import com.example.z_project.databinding.ActivityGroupCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import java.util.Calendar


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
    private lateinit var sundayDecorator: DayViewDecorator
    private lateinit var saturdayDecorator: DayViewDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()

        binding.groupCalenderExit.setOnClickListener {
            finish()
        }
    }

    private fun initView() = with(binding) {
        //recyclerViewSchedule.adapter = scheduleListAdapter
        with(calendarView) {
            // 데코레이터 초기화
            dayDecorator = CalendarDecorators.dayDecorator(this@GroupCalendarActivity)
            todayDecorator = CalendarDecorators.todayDecorator(this@GroupCalendarActivity)
            sundayDecorator = CalendarDecorators.sundayDecorator()
            saturdayDecorator = CalendarDecorators.saturdayDecorator()
            selectedMonthDecorator = CalendarDecorators.selectedMonthDecorator(
                this@GroupCalendarActivity,
                Calendar.getInstance().get(Calendar.MONTH) + 1 // Adjusting month index
            )
            // 캘린더뷰에 데코레이터 추가
            addDecorators(
                dayDecorator,
                todayDecorator,
                sundayDecorator,
                saturdayDecorator,
                selectedMonthDecorator
            )
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
                    dayDecorator,
                    todayDecorator,
                    sundayDecorator,
                    saturdayDecorator,
                    selectedMonthDecorator
                )

                // 현재 월의 첫 번째 날을 나타내는 CalendarDay 객체를 생성
                val clickedDay = CalendarDay.from(date.year, date.month, 1)
                // 캘린더 위젯에서 clickedDay를 선택하도록 지정
                widget.setDateSelected(clickedDay, true)

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
            setOnRangeSelectedListener { widget, dates -> }
            // 날짜 변경 리스너 설정
            setOnDateChangedListener { widget, date, selected ->
                val calendar = calendarDayToDate(date)
                //viewModel.filterScheduleListByDate(calendar)
            }
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
}