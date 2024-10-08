package com.example.z_project.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.chat.calendar.CalendarDecorators
import com.example.z_project.chat.calendar.ColorEnum
import com.example.z_project.chat.calendar.GroupCalendarActivity
import com.example.z_project.chat.calendar.ScheduleModel
import com.example.z_project.chat.calendar.YearSpinnerAdapter
import com.example.z_project.databinding.FragmentRecordBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import java.util.Calendar

class RecordFragment : Fragment() {
    lateinit var binding: FragmentRecordBinding

    private lateinit var dayDecorator: DayViewDecorator
    private lateinit var todayDecorator: DayViewDecorator
    private lateinit var selectedMonthDecorator: DayViewDecorator
    private lateinit var otherMonthDecorator: DayViewDecorator
    private lateinit var sundayDecorator: DayViewDecorator
    private lateinit var saturdayDecorator: DayViewDecorator
    private lateinit var recordEventDecorator: DayViewDecorator

    private val currentYear: String = Calendar.getInstance().get(Calendar.YEAR).toString()

    private var dummyScheduleList = listOf(
        FeedModel(
            id = 1,
            userImage = R.drawable.person1,
            uploadDate = "2024.10.03",
            uploadEmoji = R.drawable.ex_emoji1,
            uploadImage = R.drawable.image,
            feedText = "강아지 귀엽다",
        ),
        FeedModel(
            id = 2,
            userImage = R.drawable.person2,
            uploadDate = "2024.10.09",
            uploadEmoji = R.drawable.person4,
            uploadImage = R.drawable.image,
            feedText = "강아지",
        ),
        FeedModel(
            id = 2,
            userImage = R.drawable.person2,
            uploadDate = "2024.10.06",
            uploadEmoji = R.drawable.ex_emoji2,
            uploadImage = R.drawable.image,
            feedText = "귀엽다",
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(inflater, container, false)

        //임시 <- 최종 때 지우기
        binding.iconCalendar.setOnClickListener {
            val intent = Intent(context, GroupCalendarActivity::class.java)
            startActivity(intent)
        }

        // 캘린더 초기화
        initView()

        return binding.root
    }

    private fun initView() = with(binding) {
        //recyclerViewSchedule.adapter = scheduleListAdapter
        val exampleDate = CalendarDay.today()
        with(recordCalendarView) {
            // 데코레이터 초기화
            dayDecorator = CalendarDecorators.dayDecorator(context)
            todayDecorator = CalendarDecorators.todayDecorator(context)
            sundayDecorator = CalendarDecorators.sundayDecorator(context)
            saturdayDecorator = CalendarDecorators.saturdayDecorator(context)
            otherMonthDecorator = CalendarDecorators.otherMonthDecorator(
                context,
                Calendar.getInstance().get(Calendar.MONTH) + 1)
            selectedMonthDecorator = CalendarDecorators.selectedMonthDecorator(
                context,
                Calendar.getInstance().get(Calendar.MONTH) + 1)
            recordEventDecorator = CalendarDecorators.recordEventDecorator(
                context,
                dummyScheduleList
            )

            // 캘린더뷰에 데코레이터 추가
            addDecorators(
                //customDayViewDecorator,
                dayDecorator,
                todayDecorator,
                selectedMonthDecorator,
                otherMonthDecorator,
                sundayDecorator,
                saturdayDecorator,
                recordEventDecorator,
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
                    com.example.z_project.chat.calendar.CalendarDecorators.selectedMonthDecorator(
                        context,
                        date.month
                    )
                // 새로 생성한 데코레이터를 캘린더 위젯에 추가
                addDecorators(
                    //customDayViewDecorator,
                    dayDecorator,
                    todayDecorator,
                    selectedMonthDecorator,
                    com.example.z_project.chat.calendar.CalendarDecorators.otherMonthDecorator(
                        context,
                        date.month
                    ),
                    sundayDecorator,
                    saturdayDecorator,
                    recordEventDecorator,
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
            // 날짜 변경 리스너 설정
            setOnDateChangedListener { widget, date, selected ->
//                val filteredEvents = filterEventsByDate(date)
//                EventRVAdapter.submitList(filteredEvents)
                if(selected){
                    //날짜 선택 시 해당 날짜의 피드 모음으로 이동
                    val year = date.year
                    val month = date.month
                    val day = date.day

                    val intent = Intent(context, RecordFeedActivity::class.java).apply{
                        putExtra("selected_year", year)
                        putExtra("selected_month", month)
                        putExtra("selected_day", day)
                    }
                    binding.recordCalendarView.clearSelection() // 날짜 선택 해제
                    startActivity(intent)
                }
            }

            // Year Spinner Adapter 연결
            val years = (2000..2050).toList() // 원하는 년도 범위를 설정
            var isInitialSelected = false // Spinner의 초기값 설정을 위한 플래그

            val yearSpinnerAdapter = YearSpinnerAdapter(context, R.layout.item_spinner_year, years, currentYear)
            binding.recordYearSpinner.setAdapter(yearSpinnerAdapter)
            binding.recordYearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (isInitialSelected) {
                        yearSpinnerAdapter.setSelectedPosition(position) // 선택한 항목 설정

                        val selectedYear = years[position]
                        binding.recordCalendarView.currentDate = CalendarDay.from(selectedYear, binding.recordCalendarView.currentDate.month, 1)

                        val value = parent.getItemAtPosition(position).toString()
                        Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
                    } else{
                        isInitialSelected = true
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
            // Spinner 초기화 시 currentYear를 기본으로 설정
            val currentYearPosition = years.indexOf(currentYear.toInt())
            binding.recordYearSpinner.setSelection(currentYearPosition)
        }
    }
}