package com.example.z_project.chat.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.z_project.databinding.ActivityAddCategoryBinding
import com.example.z_project.record.RecordFeedRVAdapter

class AddCategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddCategoryBinding

    private var colorList = listOf(
        ColorEnum.YELLOW, ColorEnum.GREEN, ColorEnum.BLUE, ColorEnum.PINK,
        ColorEnum.ORANGE, ColorEnum.PURPLE, ColorEnum.RED, ColorEnum.GRAY,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이전으로 이동
        binding.addCategoryExit.setOnClickListener {
            finish()
        }


        // RecyclerView 설정 & Adapter 연결
        binding.addCategoryListRv.layoutManager = GridLayoutManager(this, 4)
        val colorAdapter = ColorAdapter(colorList) { selectedColor ->
            Toast.makeText(this, "Selected: ${selectedColor.name}", Toast.LENGTH_SHORT).show()
        }
        binding.addCategoryListRv.adapter = colorAdapter

    }

}