package com.example.z_project.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.z_project.databinding.DialogSelectCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay

class DialogSelectCategory (
    private val context: Context,
    private val calendarClearSelection: () -> Unit
){
    private val dialog = BottomSheetDialog(context)
    val bindingDialog = DialogSelectCategoryBinding.inflate(LayoutInflater.from(context))
    private val db = FirebaseFirestore.getInstance() // Firestore 인스턴스 생성
    private lateinit var categoryAdapter: CalendarCategoryRVAdapter

    @SuppressLint("ClickableViewAccessibility")
    fun show(
        categoryList: List<Categories>,
        selectedDate: CalendarDay,
        onAddEventClick: () -> Unit,
        onCategorySelected: (Categories) -> Unit,
    ) {
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // RecyclerView 설정 & Adapter 연결
        bindingDialog.selectCategoryList.layoutManager = LinearLayoutManager(context)

        // 카테고리 리스트가 비어있지 않은 경우에만 어댑터 설정
        if (categoryList.isNotEmpty()) {
//            val categoryAdapter = CalendarCategoryRVAdapter(categoryList as ArrayList<Categories>) { selectedCategory: Categories ->
//                dialog.dismiss()
//                onCategorySelected(selectedCategory)
//            }
            val categoryAdapter = CalendarCategoryRVAdapter(categoryList as ArrayList<Categories>, onCategorySelected, context)

            bindingDialog.selectCategoryList.adapter = categoryAdapter
            bindingDialog.selectCategoryList.visibility = View.VISIBLE
            bindingDialog.selectCategoryHintTv.visibility = View.GONE

            // 리사이클러뷰에 스와이프 기능 달기
            val swipeHelperCallback = SwipeHelperCallback(bindingDialog.selectCategoryList, categoryAdapter).apply {
                // 스와이프한 뒤 고정시킬 위치 지정
                val widthPixels = context.resources.displayMetrics.widthPixels
                setClamp(widthPixels.toFloat() / 7)    // 1080 / 7 = 154
            }
            val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
            itemTouchHelper.attachToRecyclerView(bindingDialog.selectCategoryList)

            // 클릭 리스너 설정
            swipeHelperCallback.attachClickListener()

            // 다른 곳 터치 시 기존 선택했던 삭제 swipe 초기화
            bindingDialog.dialogSelectCategory.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // 필요하면 터치 시작 시 동작을 처리할 수 있음 (예: UI 효과)
                    }
                    MotionEvent.ACTION_UP -> {
                        // 터치가 끝나면 스와이프 상태 초기화 및 performClick 호출
                        swipeHelperCallback.removePreviousClamp() // 스와이프 상태 초기화

                        // 키보드 숨기기
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)

                        view.performClick()  // performClick을 호출하여 클릭 이벤트를 처리하도록 보장
                    }
                }
                false  // onTouchListener에서 이벤트를 처리했음을 명시
            }
        } else {
            bindingDialog.selectCategoryList.visibility = View.GONE
            bindingDialog.selectCategoryHintTv.visibility = View.VISIBLE
        }

        // 추가 버튼 클릭 시 AddCategoryActivity 호출
        bindingDialog.selectCategoryAdd.setOnClickListener {
            onAddEventClick() // 카테고리 추가 창으로 이동
        }

        // 다이얼로그가 닫힐 때 날짜 선택 해제
        dialog.setOnDismissListener {
            calendarClearSelection()
        }

        // 다이얼로그 보여주기
        dialog.show()
    }

//    // DB 데이터 가져오기 (firebase)
//    private fun fetchCategories(selectedDate: CalendarDay) {
//        val formattedDate = formatDateWithE(selectedDate) // 원하는 형식으로 날짜 포맷팅
//        db.collection("events") // "events"는 Firestore의 컬렉션 이름
//            .whereEqualTo("startDate", formattedDate) // 날짜로 필터링
//            .addSnapshotListener { querySnapshot, exception ->
//                if (exception != null) {
//                    Log.w("DialogEventDetails", "Listen failed.", exception)
//                    return@addSnapshotListener
//                }
//
//                // 새로운 이벤트 리스트 가져오기
//                val events = querySnapshot?.toObjects(ScheduleModel::class.java) ?: emptyList()
//                eventAdapter.updateEvents(events) // 어댑터의 데이터를 업데이트
//            }
//    }
}