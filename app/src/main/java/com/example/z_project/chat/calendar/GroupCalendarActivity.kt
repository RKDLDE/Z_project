package com.example.z_project.chat.calendar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.z_project.R
import com.example.z_project.chat.calendar.CalendarDecorators.otherMonthDecorator
import com.example.z_project.databinding.ActivityGroupCalendarBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
    private lateinit var sharedPreferences: SharedPreferences


    private lateinit var groupCategoryList : List<Categories>
    private lateinit var groupCalendarEventList: List<ScheduleModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // user CODE 가져오기
        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)


//        // Firebase에서 카테고리 리스트 가져오기
//        fetchCategories("1", { categories ->
//            // 성공적으로 데이터를 받아왔을 때 groupCategoryList를 업데이트
//            groupCategoryList = categories
//            Log.d("FB 카테고리 리스트", "${groupCategoryList}")
//
//            // 필요한 UI 업데이트 작업 수행
//        }, { exception ->
//            // 실패 시 에러 처리
//            Toast.makeText(this, "Failed to load categories: ${exception.message}", Toast.LENGTH_SHORT).show()
//        })
//
//
//        // Firebase에서 일정 리스트 가져오기
//        fetchEvents("1", { events ->
//            // 성공적으로 데이터를 받아왔을 때 groupCategoryList를 업데이트
//            groupCalendarEventList = events
//            Log.d("FB 일정리스트", "${groupCalendarEventList}")
//
//            // 필요한 UI 업데이트 작업 수행
//        }, { exception ->
//            // 실패 시 에러 처리
//            Toast.makeText(this, "Failed to load events: ${exception.message}", Toast.LENGTH_SHORT).show()
//        })

        //initView()
        loadData()

        // X 아이콘 클릭 시 (캘린더 나가기)
        binding.groupCalenderExit.setOnClickListener {
            finish()
        }
    }

    // CoroutineScope를 사용하여 fetchEvents와 fetchCategory 호출
    private fun loadData() {
        lifecycleScope.launch {
//            val eventsDeferred = async { fetchEvents("1") }
//            val categoriesDeferred = async { fetchCategories("1") }

            // 월 표시 부분 커스텀 (한국어) -> 데이터를 로드한 후에 바로 적용
            binding.calendarView.setTitleFormatter(CalendarDecorators.koreanMonthTitleFormatter())

            val eventsDeferred = CompletableDeferred<List<ScheduleModel>>()
            val categoriesDeferred = CompletableDeferred<List<Categories>>()

//            val events = fetchEvents("1")
//            val categories = fetchCategories("1")
//
//            groupCalendarEventList = events
//            groupCategoryList = categories

            fetchEvents("1") { events ->
                groupCalendarEventList = events
                Log.d("일정목록들22", "${groupCalendarEventList}")
                eventsDeferred.complete(events) // 완료 시 콜백 호출
            }

            fetchCategories("1") { categories ->
                groupCategoryList = categories
                Log.d("카테고리목록들22", "${groupCategoryList}")
                categoriesDeferred.complete(categories) // 완료 시 콜백 호출
            }
            // 모든 데이터가 로드될 때까지 대기
            eventsDeferred.await()
            categoriesDeferred.await()

            // 데이터 로드 후 initView 호출
            initView()

//            Log.d("일정목록들22", "${groupCalendarEventList}")
//            Log.d("카테고리목록들22", "${groupCategoryList}")

//            // initView 호출하여 캘린더 데코레이터 등 설정
//            initView()
        }
    }

    private fun initView() = with(binding) {
        //recyclerViewSchedule.adapter = scheduleListAdapter
        val exampleDate = CalendarDay.today()
        with(calendarView) {

            Log.d("일정 잘 들어왔니", "${groupCalendarEventList}")
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
                groupCalendarEventList
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

            // 월 표시 부분 커스텀 (한국어)
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

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup any resources if needed
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
            showSelectCategoryDialog(selectedDate) // 일정 작성 다이얼로그 호출
        }
    }

    private fun showSelectCategoryDialog(selectedDate: CalendarDay) { // 카테고리 선택 창

        //Dialog 정의
        val dialogSelectCategory = DialogSelectCategory(this) {
            binding.calendarView.clearSelection() // 날짜 선택 해제
        }

        //Dialog 표시
        dialogSelectCategory.show(groupCategoryList, selectedDate, {
            // 카테고리 추가 activity 이동
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }) { selectedCategory ->
            showAddEventDialog(selectedDate, selectedCategory)
        }
    }

    private fun showAddEventDialog(selectedDate: CalendarDay, selectedCategory: Categories) { //일정 작성 dialog
        //Dialog 정의
        val dialogAddEvent = DialogAddEvent(this){
            binding.calendarView.clearSelection() // 날짜 선택 해제
        }

        //Dialog 표시
        dialogAddEvent.show(selectedDate, selectedCategory)
    }

    //날짜 확인 및 필터링 함수
    private fun filterEventsByDate(selectedDate: CalendarDay): List<ScheduleModel> {
        // 선택된 날짜를 Calendar로 변환
        val selectedCalendar = Calendar.getInstance().apply {
            set(selectedDate.year, selectedDate.month - 1, selectedDate.day, 0, 0, 0) // 시간 부분을 00:00:00으로 설정
            set(Calendar.MILLISECOND, 0) // 밀리초를 0으로 설정
        }
        val selectedDateTime = selectedCalendar.time

        return groupCalendarEventList.filter { schedule ->
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


//    // Firebase에서 카테고리 정보 가져오기
//    private suspend fun fetchCategories(groupId: String): List<Categories> {
//        val db = FirebaseFirestore.getInstance()
//        val categoryList = mutableListOf<Categories>()
//
//        // Firestore 데이터 가져오기
//        val documents = db.collection("categories")
//            .whereEqualTo("groupId", groupId) // groupId가 "1"인 문서들만 가져옴
//            .get()
//            .await() // `await()`를 사용하여 결과를 대기합니다.
//
//        if (documents != null) {
//            for (document in documents) {
//                // Firestore 문서를 Categories 객체로 변환
//                val category = document.toObject(Categories::class.java)
//                categoryList.add(category) // 리스트에 추가
//            }
//
//            // 색상 정보를 ColorEnum으로 변환하여 사용할 수 있음
//            for (category in categoryList) {
//                val colorEnum = category.getColorEnum()
//                Log.d("카테고리 색상", "Category: ${category.name}, ColorEnum: ${colorEnum}")
//            }
//
//            // categories 리스트의 내용 확인
//            for (category in categoryList) {
//                Log.d("카테고리", "Name: ${category.name}, Color: ${category.color}")
//            }
//        } else {
//            Log.d("fetchCategories", "No Category documents found for groupId: $groupId")
//        }
//
//        return categoryList // 리스트 반환
//    }
//
//
//    // Firebase에서 일정 정보 가져오기
//    private suspend fun fetchEvents(groupId: String): List<ScheduleModel> {
//        val db = FirebaseFirestore.getInstance()
//        val eventList = mutableListOf<ScheduleModel>()
//
//        // Firestore 데이터 가져오기
//        val documents = db.collection("events")
//            .whereEqualTo("groupId", groupId) // groupId가 "1"인 문서들만 가져옴
//            .get()
//            .await() // `await()`를 사용하여 결과를 대기합니다.
//
//        // Firestore 문서를 ScheduleModel 객체로 변환
//        for (document in documents) {
//            val event = document.toObject(ScheduleModel::class.java).apply{
//                documentId = document.id
//            }
//            eventList.add(event) // 리스트에 추가
//        }
//
//        // categories 리스트의 내용 확인
//        for (event in eventList) {
//            Log.d("캘린더 일정", "Name: ${event.title}")
//        }
//
//        return eventList // 리스트 반환
//    }


    // Firebase에서 이벤트 정보를 실시간으로 가져오는 메서드
    private fun fetchEvents(groupId: String, onSuccess: (List<ScheduleModel>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Firestore의 events 컬렉션에 대한 실시간 리스너 추가
        db.collection("events")
            .whereEqualTo("groupId", groupId)
            .addSnapshotListener { documents, error ->
                if (error != null) {
                    Log.w("fetchEvents", "Listen failed.", error)
                    return@addSnapshotListener
                }

                val eventList = mutableListOf<ScheduleModel>()
                documents?.forEach { document ->
                    val event = document.toObject(ScheduleModel::class.java).apply {
                        documentId = document.id
                    }
                    eventList.add(event) // 리스트에 추가
                }

                // 이벤트 목록 업데이트
                onSuccess(eventList)
            }
    }

    // Firebase에서 카테고리 정보를 실시간으로 가져오는 메서드
    private fun fetchCategories(groupId: String, onSuccess: (List<Categories>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Firestore의 categories 컬렉션에 대한 실시간 리스너 추가
        db.collection("categories")
            .whereEqualTo("groupId", groupId)
            .addSnapshotListener { documents, error ->
                if (error != null) {
                    Log.w("fetchCategories", "Listen failed.", error)
                    return@addSnapshotListener
                }

                val categoryList = mutableListOf<Categories>()
                documents?.forEach { document ->
                    val category = document.toObject(Categories::class.java)
                    categoryList.add(category) // 리스트에 추가
                }

                // 카테고리 목록 업데이트
                onSuccess(categoryList)
            }
    }
}