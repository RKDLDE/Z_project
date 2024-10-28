package com.example.z_project.record

import android.content.Context
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
import com.example.z_project.chat.calendar.GroupCalendarActivity
import com.example.z_project.databinding.FragmentRecordBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.z_project.chat.calendar.YearSpinnerAdapter

class RecordFragment : Fragment() {
    private lateinit var binding: FragmentRecordBinding
    private lateinit var recordFeedRVAdapter: RecordFeedRVAdapter
    private val firestore = FirebaseFirestore.getInstance()

//    // 현재 년도와 월
//    private val currentYear: String = Calendar.getInstance().get(Calendar.YEAR).toString()
//    private val currentMonth: String = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(inflater, container, false)

        // 임시 - 캘린더 클릭 시 GroupCalendarActivity로 이동
        binding.iconCalendar.setOnClickListener {
            val intent = Intent(context, GroupCalendarActivity::class.java)
            startActivity(intent)
        }

//        // Spinner 초기화
//        initYearSpinner()
//        initMonthSpinner()

        // RecyclerView 설정
        binding.recordFeedRv.layoutManager = GridLayoutManager(context, 3)
        recordFeedRVAdapter = RecordFeedRVAdapter(emptyList()) // 빈 리스트로 초기화
        binding.recordFeedRv.adapter = recordFeedRVAdapter // Adapter 연결

        // Firestore 데이터 가져오기
        fetchData()

        return binding.root
    }

//    private fun initMonthSpinner() = with(binding) {
//        val months = (1..12).toList() // 1부터 12까지의 월 리스트
//
//        var isMonthInitialSelected = false // Spinner의 초기값 설정을 위한 플래그
//        Log.d("현재 달월", "${currentMonth}")
//
//        val monthSpinnerAdapter = YearSpinnerAdapter(requireContext(), R.layout.item_spinner_year, months, currentMonth)
//        binding.recordMonthSpinner.setAdapter(monthSpinnerAdapter)
//        binding.recordMonthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                if (isMonthInitialSelected) {
//                    monthSpinnerAdapter.setSelectedPosition(position) // 선택한 항목 설정
//
//                    val selectedMonth = months[position]
//                    val value = parent.getItemAtPosition(position).toString()
//                    Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
//                } else {
//                    isMonthInitialSelected = true
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//        // Spinner 초기화 시 currentMonth를 기본으로 설정
//        val currentMonthPosition = months.indexOf(currentMonth.toInt())
//        binding.recordMonthSpinner.setSelection(currentMonthPosition)
//    }
//
//    private fun initYearSpinner() = with(binding) {
//        val years = (2024..2050).toList() // 원하는 년도 범위 설정
//        var isYearInitialSelected = false // Spinner의 초기값 설정을 위한 플래그
//        Log.d("현재 년도", "${currentYear}")
//
//        val yearSpinnerAdapter = RecordYearSpinnerAdapter(requireContext(), R.layout.item_record_spinner_year, years, currentYear)
//        binding.recordYearSpinner.setAdapter(yearSpinnerAdapter)
//        binding.recordYearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                if (isYearInitialSelected) {
//                    yearSpinnerAdapter.setSelectedPosition(position) // 선택한 항목 설정
//
//                    val selectedYear = years[position]
//                    val value = parent.getItemAtPosition(position).toString()
//                    Toast.makeText(context, value, Toast.LENGTH_SHORT).show()
//                } else {
//                    isYearInitialSelected = true
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//        // Spinner 초기화 시 currentYear를 기본으로 설정
//        val currentYearPosition = years.indexOf(currentYear.toInt())
//        binding.recordYearSpinner.setSelection(currentYearPosition)
//    }

    private fun fetchData() {
        // SharedPreferences에서 uniqueCodes 가져오기
        val sharedPreferences = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)

        Log.d("RecordFeed", "내 코드: ${uniqueCode}")
        // Firestore에서 친구 목록 가져오기
        firestore.collection("friends")
            .document(uniqueCode!!)
            .collection("friendsList")
            .get()
            .addOnSuccessListener { friendListSnapshot ->
                // 친구 코드를 리스트로 저장
                val friendCodes = friendListSnapshot.documents.mapNotNull { it.getString("friendCode") }

                // 자신의 uniqueCode와 친구 코드 리스트를 합침
                val uniqueCodes = mutableListOf(uniqueCode)
                uniqueCodes.addAll(friendCodes)

                Log.d("RecordFeed", "총 uniqueCodes: $uniqueCodes")

                if (uniqueCodes.isNotEmpty()) {
                    firestore.collection("images")
                        .whereIn("uniqueCode", uniqueCodes)
                        .orderBy("uploadTime", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { result ->
                            if (result.isEmpty) {
                                Log.d("RecordFragment", "No documents found.")
                                return@addOnSuccessListener
                            }

                            val feedItems = mutableListOf<FeedModel>()
                            val fetchCount = result.size() // 결과 개수 저장
                            Log.d("RecordFeed", "피드 개수: $fetchCount")

                            for (document in result) {
                                val uploadImage = document.getString("UploadImageUrl") ?: ""
                                val uniqueCode = document.getString("uniqueCode") ?: ""
                                val uploadEmoji = document.getString("emoji") ?: ""
                                val feedText = document.getString("inputText") ?: ""
                                val uploadTime = document.getTimestamp("uploadTime") ?: Timestamp.now()

                                // 사용자 프로필 이미지를 가져오기 위해 Firestore 호출
                                firestore.collection("users").document(uniqueCode).get()
                                    .addOnSuccessListener { userResult ->
                                        if (userResult.exists()) {
                                            val profileImage = userResult.getString("profileImage") ?: ""
                                            val uploadDate = formatTimestampToString(uploadTime)
                                            val userName = userResult.getString("name") ?: ""

                                            val feedItem = FeedModel(
                                                uniqueCode = uniqueCode,
                                                userName = userName,
                                                profileImage = profileImage,
                                                uploadDate = uploadDate,
                                                uploadImage = uploadImage,
                                                uploadEmoji = uploadEmoji,
                                                feedText = feedText
                                            )
                                            feedItems.add(feedItem)

                                            // 모든 친구의 프로필 이미지를 가져오면 Adapter 업데이트
                                            // 모든 친구의 프로필 이미지를 가져오면 Adapter 업데이트
                                            if (feedItems.size == fetchCount) {
                                                feedItems.sortByDescending { it.uploadDate }
                                                recordFeedRVAdapter.updateItems(feedItems)

                                                // 클릭 리스너 설정
                                                recordFeedRVAdapter.onItemClick = { feedItem ->
                                                    val bundle = Bundle().apply {
                                                        Log.d("click", "uploadEmoji: ${feedItem.uploadEmoji}")
                                                        Log.d("click", "feedText: ${feedItem.feedText}")
                                                        Log.d("click", "uploadImage: ${feedItem.uploadImage}")
                                                        Log.d("click", "userName: ${feedItem.userName}")
                                                        Log.d("click", "uploadDate: ${feedItem.uploadDate}")
                                                        Log.d("click", "profileImage: ${feedItem.profileImage}")

                                                        putString("uploadEmoji", feedItem.uploadEmoji)
                                                        putString("feedText", feedItem.feedText)
                                                        putString("uploadImage", feedItem.uploadImage)
                                                        putString("userName", feedItem.userName)
                                                        putString("uploadDate", feedItem.uploadDate)
                                                        putString("profileImage", feedItem.profileImage)
                                                    }

                                                    // FeedFinalFragment로 이동
                                                    val feedFinalFragment = FeedFinalFragment().apply {
                                                        arguments = bundle
                                                    }

                                                    // Fragment 전환
                                                    parentFragmentManager.beginTransaction()
                                                        .replace(R.id.main_frm, feedFinalFragment) // fragment_container는 실제 Container의 ID로 수정
                                                        .addToBackStack(null)
                                                        .commit()
                                                }
                                                }
                                        }
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.w("RecordFragment", "Error getting documents: ", e)
                        }
                } else {
                    Log.w("RecordFragment", "No unique codes available for query.")
                }
            }
            .addOnFailureListener { e ->
                Log.w("RecordFragment", "Error getting friend list: ", e)
            }
    }

    // Timestamp를 문자열로 변환하는 함수
    private fun formatTimestampToString(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(timestamp.toDate()) // Timestamp를 Date로 변환 후 문자열로 변환
    }
}
