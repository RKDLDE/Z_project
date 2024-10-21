package com.example.z_project.chat.calendar

import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import kotlin.math.min

// 롱터치 후 드래그, 스와이프 동작 제어
class SwipeHelperCallback(private val recyclerView: RecyclerView, private val recyclerViewAdapter : RecyclerView.Adapter<*>)
    : ItemTouchHelper.Callback() {

    // swipe_view 를 swipe 했을 때 <삭제> 화면이 보이도록 고정하기 위한 변수들
    var currentPosition: Int? = null    // 현재 선택된 recycler view의 position
    private var previousPosition: Int? = null   // 이전에 선택했던 recycler view의 position
    private var currentDx = 0f                  // 현재 x 값
    private var clamp = 0f                      // 고정시킬 크기

    // 이동 방향 결정하기
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // 드래그 방향 : 위, 아래 인식
        // 스와이프 방향 : 왼쪽, 오른쪽 인식
        // 설정 안 하고 싶으면 0
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    // 드래그 일어날 때 동작 (롱터치 후 드래그)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
//        // 리사이클러뷰에서 현재 선택된 데이터와 드래그한 위치에 있는 데이터를 교환
//        val fromPos: Int = viewHolder.adapterPosition
//        val toPos: Int = target.adapterPosition
//        recyclerViewAdapter.swapData(fromPos, toPos)
//        return true

        return false
    }

    // 스와이프 일어날 때 동작
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 스와이프 끝까지 하면 해당 데이터 삭제하기 -> 스와이프 후 <삭제> 버튼 눌러야 삭제 되도록 변경
        // recyclerViewAdapter.removeData(viewHolder.layoutPosition)
    }

    // 다른 영역 클릭시 스와이프 초기화
    fun attachClickListener() {
        recyclerView.setOnClickListener {
            removePreviousClamp() // 활성화된 스와이프 상태 리셋
        }
        recyclerView.setOnTouchListener { _, _ ->
            removePreviousClamp() // 활성화된 스와이프 상태 리셋
            false
        }
//        recyclerView.setOnClickListener {
//            removePreviousClamp() // 활성화된 스와이프 상태 리셋
//        }
//
//        // 특정 뷰 (예: erase_item_view) 클릭 리스너 추가
//        recyclerView.findViewById<View>(R.id.erase_item_view).setOnClickListener { view ->
//            val viewHolder = recyclerView.getChildViewHolder(view.parent as View) // erase_item_view의 부모로부터 ViewHolder 가져오기
//            if (getTag(viewHolder)) { // 스와이프가 완료된 상태인지 확인
//                recyclerViewAdapter.removeData(viewHolder.layoutPosition)
//            }
//        }
    }

    // -------------swipe 됐을 때 일어날 동작---------------
    // swipe_view만 슬라이드 되도록 + 일정 범위를 swipe하면 <삭제> 화면 보이게 하기

    // 사용자와의 상호작용과 해당 애니메이션도 끝났을 때 호출
    // drag된 view가 drop 되었거나, swipe가 cancel되거나 complete되었을 때 호출
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        currentDx = 0f                                      // 현재 x 위치 초기화
        previousPosition = viewHolder.adapterPosition       // 드래그 또는 스와이프 동작이 끝난 view의 position 기억하기
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    // ItemTouchHelper가 ViewHolder를 스와이프 되었거나 드래그 되었을 때 호출
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition    // 현재 드래그 또는 스와이프 중인 view 의 position 기억하기
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    // 아이템을 터치하거나 스와이프하는 등 뷰에 변화가 생길 경우 호출
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val eraseView = getEraseView(viewHolder)
            val isClamped = getTag(viewHolder)      // 고정할지 말지 결정, true : 고정함 false : 고정 안 함
            val newX = clampViewPositionHorizontal(dX, isClamped, isCurrentlyActive)  // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)

//            // 스와이프 완료 상태를 관리
//            if (dX < -clamp) {
//                setTag(viewHolder, true) // 스와이프가 완료된 상태
//            } else {
//                setTag(viewHolder, false) // 스와이프가 완료되지 않은 상태
//            }

            // 스와이프 완료 여부에 따라 erase_item_view 클릭 가능 여부 설정
            Log.d("완료여부", "${isClamped}")
            eraseView.isClickable = isClamped

            // 클릭 리스너를 상태에 따라 동적으로 설정/제거
            if (isClamped) {
                eraseView.setOnClickListener { view ->
                    // 스와이프가 완료된 경우에만 삭제 처리
                    if (recyclerViewAdapter is EventRVAdapter) {
                        (recyclerViewAdapter as EventRVAdapter).removeData(viewHolder.layoutPosition)
                    } else if (recyclerViewAdapter is CalendarCategoryRVAdapter){
                        (recyclerViewAdapter as CalendarCategoryRVAdapter).removeData(viewHolder.layoutPosition)
                    }
                    Toast.makeText(viewHolder.itemView.context, "일정이 삭제됐습니다", Toast.LENGTH_SHORT).show()
                }
            } else {
                eraseView.setOnClickListener(null)  // 스와이프가 완료되지 않았을 때는 클릭 불가
            }

            // 고정시킬 시 애니메이션 추가
            if (newX == -clamp) {
                getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
                return
            }

            currentDx = newX
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                newX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    // 사용자가 view를 swipe 했다고 간주할 최소 속도 정하기
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = defaultValue * 10

    // 사용자가 view를 swipe 했다고 간주하기 위해 이동해야하는 부분 반환
    // (사용자가 손을 떼면 호출됨)
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        // -clamp 이상 swipe시 isClamped를 true로 변경 아닐시 false로 변경
        setTag(viewHolder, currentDx <= -clamp)
        return 2f
    }

    // swipe_view 반환 -> swipe_view만 이동할 수 있게 해줌
    private fun getView(viewHolder: RecyclerView.ViewHolder) : View = viewHolder.itemView.findViewById(
        R.id.item_calendar_event)

    // erase_item_view 찾는 함수 추가
    private fun getEraseView(viewHolder: RecyclerView.ViewHolder): View {
        return viewHolder.itemView.findViewById(R.id.erase_item_view)
    }

    // swipe_view 를 swipe 했을 때 <삭제> 화면이 보이도록 고정
    private fun clampViewPositionHorizontal(
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {
        // RIGHT 방향으로 swipe 막기
        val max = 0f

        // 고정할 수 있으면
        val newX = if (isClamped) {
            // 현재 swipe 중이면 swipe되는 영역 제한
            if (isCurrentlyActive)
            // 오른쪽 swipe일 때
                if (dX < 0) dX/3 - clamp
                // 왼쪽 swipe일 때
                else dX - clamp
            // swipe 중이 아니면 고정시키기
            else -clamp
        }
        // 고정할 수 없으면 newX는 스와이프한 만큼
        else dX / 2

        // newX가 0보다 작은지 확인
        return min(newX, max)
    }

    // isClamped를 view의 tag로 관리
    // isClamped = true : 고정, false : 고정 해제
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }
    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
        return viewHolder.itemView.tag as? Boolean ?: false
    }


    // view가 swipe 되었을 때 고정될 크기 설정
    fun setClamp(clamp: Float) { this.clamp = clamp }

    // 다른 View가 swipe되거나 터치되면 고정 해제
    internal fun removePreviousClamp() {
        // 현재 선택한 view가 이전에 선택한 view와 같으면 패스
        if (currentPosition == previousPosition) return

        // 이전에 선택한 위치의 view 고정 해제
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).animate().x(0f).setDuration(100L).start()
            setTag(viewHolder, false)
            previousPosition = null
        }
    }
}