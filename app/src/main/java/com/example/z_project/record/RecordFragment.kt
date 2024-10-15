package com.example.z_project.record

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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

    private val currentYear: String = Calendar.getInstance().get(Calendar.YEAR).toString()
    private val currentMonth: String = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()

    var dummyList = listOf(
        FeedModel(
            id = 1,
            userImage = R.drawable.person1,
            uploadDate = "2024.10.07",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워",
        ),
        FeedModel(
            id = 2,
            userImage = R.drawable.person2,
            uploadDate = "2024.10.03",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 3,
            userImage = R.drawable.person3,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),
        FeedModel(
            id = 4,
            userImage = R.drawable.person4,
            uploadDate = "2024.10.15",
            uploadImage = R.drawable.image,
            uploadEmoji = R.drawable.emoji,
            feedText = "강아지 귀여워"
        ),

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

        // 스피너 초기화
        initYearSpinner()
        initMonthSpinner()

        // RecyclerView 설정 & Adapter 연결
        binding.recordFeedRv.layoutManager = GridLayoutManager(context, 3)
        val eventAdapter = RecordFeedRVAdapter(dummyList)
        binding.recordFeedRv.adapter = eventAdapter


        return binding.root
    }

    private fun initMonthSpinner() = with(binding) {
        //recyclerViewSchedule.adapter = scheduleListAdapter
        val exampleDate = CalendarDay.today()

        // Month Spinner Adapter 연결
        val months = (1..12).toList() // 원하는 범위를 설정
        var isMonthInitialSelected = false // Spinner의 초기값 설정을 위한 플래그

        Log.d("현재 달월", "${currentMonth}")

        val monthSpinnerAdapter = YearSpinnerAdapter(requireContext(), R.layout.item_spinner_year, months, currentMonth)
        binding.recordMonthSpinner.setAdapter(monthSpinnerAdapter)
        binding.recordMonthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isMonthInitialSelected) {
                    monthSpinnerAdapter.setSelectedPosition(position) // 선택한 항목 설정

                    val selectedMonth = months[position]

                    val value = parent.getItemAtPosition(position).toString()
                    Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
                } else{
                    isMonthInitialSelected = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        // Spinner 초기화 시 currentYear를 기본으로 설정
        val currentMonthPosition = months.indexOf(currentMonth.toInt())
        binding.recordMonthSpinner.setSelection(currentMonthPosition)
    }

    private fun initYearSpinner() = with(binding) {
        //recyclerViewSchedule.adapter = scheduleListAdapter
        val exampleDate = CalendarDay.today()

        val years = (2024..2050).toList() // 원하는 년도 범위를 설정
        var isYearInitialSelected = false // Spinner의 초기값 설정을 위한 플래그

        Log.d("현재 년도", "${currentYear}")

        val yearSpinnerAdapter = RecordYearSpinnerAdapter(requireContext(), R.layout.item_record_spinner_year, years, currentYear)
        binding.recordYearSpinner.setAdapter(yearSpinnerAdapter)
        binding.recordYearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isYearInitialSelected) {
                    yearSpinnerAdapter.setSelectedPosition(position) // 선택한 항목 설정

                    val selectedMonth = years[position]

                    val value = parent.getItemAtPosition(position).toString()
                    Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
                } else{
                    isYearInitialSelected = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        // Spinner 초기화 시 currentYear를 기본으로 설정
        val currentYearPosition = years.indexOf(currentMonth.toInt())
        binding.recordYearSpinner.setSelection(currentYearPosition)
    }
}