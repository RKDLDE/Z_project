package com.example.z_project.record

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.z_project.R
import com.example.z_project.databinding.ActivityRecordFeedBinding

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
            )

        )

        // RecyclerView 설정 & Adapter 연결
        binding.recordFeedRv.layoutManager = GridLayoutManager(this, 3)
        val eventAdapter = RecordFeedRVAdapter(dummyList)
        binding.recordFeedRv.adapter = eventAdapter

    }
}