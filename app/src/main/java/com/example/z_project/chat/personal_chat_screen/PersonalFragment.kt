package com.example.z_project.chat.personal_chat_screen

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment

class PersonalFragment : Fragment() {
    private fun fetchData() {
        // SharedPreferences에서 uniqueCodes 가져오기
        val sharedPreferences =
            requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)
        val friendCodes =
            sharedPreferences.all.keys.filter { (it != "UNIQUE_CODE") && (it != "REFRESH_TOKEN") }

        Log.d("RecordFeed", "내 코드: ${uniqueCode}")
        Log.d("RecordFeed", "친구 코드: ${friendCodes}")

    }
}