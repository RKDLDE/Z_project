package com.example.z_project.calendar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.z_project.R
import com.example.z_project.calendar.CalendarDecorators.otherMonthDecorator
import com.example.z_project.databinding.FragmentCalendarBinding
import com.example.z_project.mypage.FriendData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

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
    private var uniqueCode : String? = null
    private lateinit var userName : String
    private lateinit var userProfile : String
    private lateinit var selectCode : String

    private var categoryList = mutableListOf<Categories>()
    private var calendarEventList = mutableListOf<ScheduleModel>()
    private var friendList = mutableListOf<FriendData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // user CODE 가져오기
        sharedPreferences = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)

        // 첫 시작, 초기화
        selectCode = uniqueCode!!


        // Spinner 초기화
        fetchFriendList(uniqueCode!!) { userName ->
            Log.d("사용자이름을말해라!", "${userName}")
            var isInitialSelected = false // Spinner의 초기값 설정을 위한 플래그

            Log.d("CalendarFragment", "friendList: $friendList")

            val friendsListAdapter = FriendsListAdapter(
                requireContext(), R.layout.item_spinner_year, friendList, userName
            )
            binding.friendsListSpinner.adapter = friendsListAdapter
            binding.friendsListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (isInitialSelected) {
                        friendsListAdapter.setSelectedPosition(position) // 선택한 항목 설정

                        selectCode = friendList[position].code

                        lifecycleScope.launch {
                            loadData(selectCode)  // 선택한 친구의 캘린더 데이터로 갱신
                        }

                        val value = friendList[position].name
                        if(selectCode != uniqueCode){
                            Toast.makeText(requireContext(), "${value}의 캘린더를 조회", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        isInitialSelected = true
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            // Spinner 초기화 시 사용자 본인의 이름을 기본으로 설정
            val currentPosition = friendList.indexOfFirst { it.name == userName }
            binding.friendsListSpinner.setSelection(currentPosition)
        }


        // Firebase 데이터 로드 및 캘린더 초기화
        loadData(uniqueCode!!)
    }

    private fun loadData(uniqueCode: String) {
        // 초기화 작업
        categoryList.clear()
        calendarEventList.clear()

        // 캘린더를 잠시 숨김
        binding.fragmentCalendar.visibility = View.GONE

        lifecycleScope.launch {
            val eventsDeferred = CompletableDeferred<List<ScheduleModel>>()
            val categoriesDeferred = CompletableDeferred<List<Categories>>()

            // 월 표시 부분 커스텀 (한국어) -> 데이터를 로드한 후에 바로 적용
            binding.calendarView.setTitleFormatter(CalendarDecorators.koreanMonthTitleFormatter())

            fetchEvents(uniqueCode) { events ->
                calendarEventList = events.toMutableList()
                Log.d("일정목록들22", "$calendarEventList")
                eventsDeferred.complete(events)
                updateCalendarDecorators() // 캘린더 데코레이터 업데이트
            }

            fetchCategories(uniqueCode) { categories ->
                categoryList = categories.toMutableList()
                Log.d("카테고리목록들22", "$categoryList")
                categoriesDeferred.complete(categories)
            }

            // 모든 데이터가 로드될 때까지 대기
            eventsDeferred.await()
            categoriesDeferred.await()

            // 데이터 로드 후 initView 호출
            initView()
            Log.d("데이터로드", "${calendarEventList.size}")
            binding.fragmentCalendar.visibility = View.VISIBLE
        }
    }
    private fun updateCalendarDecorators() {
        // 캘린더의 dotSpan 업데이트하는 로직
        // 기존 데코레이터를 제거하고 새로 추가
        binding.calendarView.removeDecorators()
        eventDecorator = CalendarDecorators.eventDecorator(
            requireContext(), calendarEventList)
        binding.calendarView.addDecorator(eventDecorator)
    }

    private fun initView() = with(binding) {
        val exampleDate = CalendarDay.today()
        with(calendarView) {
            Log.d("일정 잘 들어왔니", "$calendarEventList")

            // 일정 초기화!
            clearSelection() // 캘린더 위젯에서 현재 선택된 날짜를 모두 선택 해제
            removeDecorators() // 캘린더 위젯에 적용된 모든 데코레이터를 제거
            invalidateDecorators() // 데코레이터가 제거되고 위젯이 다시 그려지도록

            // 데코레이터 생성
            dayDecorator = CalendarDecorators.dayDecorator(requireContext())
            todayDecorator = CalendarDecorators.todayDecorator(requireContext())
            sundayDecorator = CalendarDecorators.sundayDecorator(requireContext())
            saturdayDecorator = CalendarDecorators.saturdayDecorator(requireContext())
            otherMonthDecorator = CalendarDecorators.otherMonthDecorator(
                requireContext(), Calendar.getInstance().get(Calendar.MONTH) + 1)
            selectedMonthDecorator = CalendarDecorators.selectedMonthDecorator(
                requireContext(), Calendar.getInstance().get(Calendar.MONTH) + 1)
            eventDecorator = CalendarDecorators.eventDecorator(
                requireContext(), calendarEventList)


            // 캘린더뷰에 데코레이터 추가
            addDecorators(
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
                widget.clearSelection() // 캘린더 위젯에서 현재 선택된 날짜를 모두 선택 해제
                widget.removeDecorators() // 캘린더 위젯에 적용된 모든 데코레이터를 제거
                widget.invalidateDecorators() // 데코레이터가 제거되고 위젯이 다시 그려지도록


                // 새로운 월에 해당하는 데코레이터를 생성하여 selectedMonthDecorator에 할당
                selectedMonthDecorator =
                    CalendarDecorators.selectedMonthDecorator(requireContext(), date.month)
                addDecorators( // 새로 생성한 데코레이터를 캘린더 위젯에 추가
                    dayDecorator,
                    todayDecorator,
                    selectedMonthDecorator,
                    otherMonthDecorator(requireContext(), date.month),
                    sundayDecorator,
                    saturdayDecorator,
                    eventDecorator,
                )
            }

            // 요일 텍스트 포메터 설정
            setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

            // 헤더 텍스트 모양 설정
            setHeaderTextAppearance(R.style.CalendarWidgetHeader)

            // 범위 선택 리스너 설정
            setOnRangeSelectedListener { widget, dates ->
                if (dates.isNotEmpty()) showEventDetailsDialog() // 날짜 범위가 선택되었을 때 showEventDetails() 호출
            }

            // 날짜 변경 리스너 설정
            setOnDateChangedListener { widget, date, selected ->
                if (selected) showEventDetailsDialog() // 날짜 선택되었을 때 showEventDetails() 호출
            }
        }
    }

    // 친구 목록 불러오는 함수
    fun fetchFriendList(uniqueCode: String, onComplete: (String) -> Unit) {
        if (uniqueCode.isNotEmpty()) {
            db.collection("users").document(uniqueCode).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userName = document.getString("name") ?: "이름 없음"
                        userProfile = document.getString("profileImage") ?: ""

                        // 본인 데이터를 friendList에 추가
                        friendList.add(FriendData(uniqueCode, userName, userProfile))

                        db.collection("friends").document(uniqueCode)
                            .collection("friendsList")
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                val loadTasks = querySnapshot.documents.map { document ->
                                    val friendCode = document.getString("friendCode") ?: return@map null

                                    db.collection("users").document(friendCode)
                                        .get()
                                        .continueWith { task ->
                                            val userDocument = task.result
                                            if (userDocument != null && userDocument.exists()) {
                                                val friendName = userDocument.getString("name") ?: "이름 없음"
                                                val friendProfileImage = userDocument.getString("profileImage") ?: ""
                                                friendList.add(FriendData(friendCode, friendName, friendProfileImage))
                                            }
                                        }
                                }.filterNotNull() // null 값 제거

                                // 모든 친구 데이터를 불러온 후 콜백 호출
                                Tasks.whenAll(loadTasks).addOnSuccessListener {
                                    onComplete(userName)
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("FriendList", "친구 목록 조회 실패: ${exception.message}")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("userInfo", "데이터 가져오기 실패", exception)
                }
        } else {
            Log.e("FriendList", "고유 코드가 존재하지 않습니다.")
        }
    }


    //일정 목록 확인 Dialog
    private fun showEventDetailsDialog() {
        val selectedDate = binding.calendarView.selectedDate // 선택된 날짜 가져오기
        val filteredEvents = filterEventsByDate(selectedDate!!)
        Log.d("선택날짜", "$selectedDate")
        Log.d("스케줄내용", "$filteredEvents")

        //Dialog 정의
        val dialogEventDetails = DialogEventDetails(requireContext()) {
            binding.calendarView.clearSelection()
        }

        // 선택된 친구의 코드와 사용자 ID가 같을 경우에만 아이콘을 보이게 설정
        dialogEventDetails.setAddCalendarIconVisibility(selectCode == uniqueCode)

        // 조건 추가: selectCode와 uniqueCode가 같지 않고 filteredEvents가 비어있는 경우
        if (selectCode != uniqueCode && filteredEvents.isEmpty()) {
            // 아무것도 하지 않고 함수를 종료
            binding.calendarView.clearSelection()
            return
        }

        //Dialog 표시
        dialogEventDetails.show(filteredEvents, selectedDate) {
            showSelectCategoryDialog(selectedDate)
        }
    }


    // 카테고리 선택 Dialog
    private fun showSelectCategoryDialog(selectedDate: CalendarDay) {
        //Dialog 정의
        val dialogSelectCategory = DialogSelectCategory(requireContext()) {
            binding.calendarView.clearSelection() // 선택 날짜 해제
        }

        //Dialog 표시
        dialogSelectCategory.show(categoryList, selectedDate, {
            // '추가' 클릭시 카테고리 추가 activity 이동
            val intent = Intent(requireContext(), AddCategoryActivity::class.java)
            startActivity(intent)

        }) { selectedCategory ->
            showAddEventDialog(selectedDate, selectedCategory)
        }
    }


    //일정 작성 dialog
    private fun showAddEventDialog(selectedDate: CalendarDay, selectedCategory: Categories) {
        //Dialog 정의
        val dialogAddEvent = DialogAddEvent(requireContext(),
            calendarClearSelection = {
                binding.calendarView.clearSelection() // 선택 날짜 해제
            }
        )


        //Dialog 표시
        dialogAddEvent.show(selectedDate, selectedCategory)
    }


    // 선택 날짜 확인 및 해당하는 이벤트 필터링 함수
    private fun filterEventsByDate(selectedDate: CalendarDay): List<ScheduleModel> {
        // 선택된 날짜를 Calendar로 변환
        val selectedCalendar = Calendar.getInstance().apply {
            set(selectedDate.year, selectedDate.month - 1, selectedDate.day, 0, 0, 0) // 시간 부분을 00:00:00으로 설정
            set(Calendar.MILLISECOND, 0) // 밀리초를 0으로 설정
        }
        val selectedDateTime = selectedCalendar.time

        return calendarEventList.filter { schedule ->
            schedule.startDate?.let { startDate ->
                schedule.endDate?.let { endDate ->
                    try {
                        // startDate와 endDate를 Calendar로 변환
                        val startCalendar = Calendar.getInstance().apply {
                            time = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).parse(startDate)
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val endCalendar = Calendar.getInstance().apply {
                            time = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).parse(endDate)
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


    // Firebase에서 이벤트 정보를 실시간으로 가져오는 메서드
    private fun fetchEvents(uniqueCode: String, onSuccess: (List<ScheduleModel>) -> Unit) {
        // Firestore의 events 컬렉션에 대한 실시간 리스너 추가
        db.collection("events")
            .whereEqualTo("authId", uniqueCode)
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
    private fun fetchCategories(uniqueCode: String, onSuccess: (List<Categories>) -> Unit) {
        // Firestore의 categories 컬렉션에 대한 실시간 리스너 추가
        db.collection("categories")
            .whereEqualTo("authId", uniqueCode)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
