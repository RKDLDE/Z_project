package com.example.z_project.record

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.z_project.R
import com.example.z_project.databinding.ActivityRecordFeedBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RecordFeedActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecordFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 클릭 기능
        binding.recordFeedBackIcon.setOnClickListener {
            finish()
        }

        // 선택한 날짜의 정보 받기
        val selectedYear = intent.getIntExtra("selected_year", -1)
        val selectedMonth = intent.getIntExtra("selected_month", -1)
        val selectedDay = intent.getIntExtra("selected_day", -1)

        if (selectedYear != -1 && selectedMonth != -1 && selectedDay != -1) {
            val selectedDate = CalendarDay.from(selectedYear, selectedMonth, selectedDay)

            // 선택한 날짜 텍스트로 표시
            binding.recordFeedDate.text = formatDateWithE(selectedDate)
        }

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

        // RecyclerView 설정 & Adapter 연결
        binding.recordFeedRv.layoutManager = GridLayoutManager(this, 3)
        val eventAdapter = RecordFeedRVAdapter(dummyList)
        binding.recordFeedRv.adapter = eventAdapter

    }

    private fun formatDateWithE(date: CalendarDay): String {
        val calendar = Calendar.getInstance().apply {
            set(date.year, date.month - 1, date.day)  // MaterialCalendarView uses 1-based months
        }
        val format = SimpleDateFormat("M월 d일", Locale("ko", "KR"))
        return format.format(calendar.time)
    }
}