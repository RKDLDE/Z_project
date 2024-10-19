package com.example.z_project.chat.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.z_project.R
import com.example.z_project.databinding.ItemCalendarEventBinding
import com.google.firebase.firestore.FirebaseFirestore

class EventRVAdapter (
    private var events: ArrayList<ScheduleModel>,
    private val context: Context
) : RecyclerView.Adapter<EventRVAdapter.ViewHolder>(){

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemCalendarEventBinding = ItemCalendarEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position])
    }

    fun submitList(newEvents: List<ScheduleModel>) {
        events = newEvents as ArrayList<ScheduleModel>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = events.size

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("UNIQUE_CODE", null)
        val event = events[position]

        if (event.authId == userId && event.groupId == "1") {
            firestore.collection("events")
                .document(event.documentId!!)
                .delete()
                .addOnSuccessListener {
                    Log.d("Firestore", "Document successfully deleted!")
                    events.removeAt(position)
                    notifyItemRemoved(position)
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error deleting document", e)
                    Toast.makeText(context, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "삭제 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ViewHolder(val binding: ItemCalendarEventBinding, private val context: Context):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceType")
        fun bind(event: ScheduleModel) {
            binding.calendarEventContent.text = event.title
            Log.d("일정내용", "${event.title}")
            Log.d("카테고리색상", "categoryColor: ${event.category.color}")

            event.category.let {
                Log.d("카테고리 색깔", "${it.color}")
                val colorEnum = it.getColorEnum()
                val colorResId = colorEnum!!.color ?: R.color.main_gray // null일 경우 기본 색상 설정
                val color = ContextCompat.getColor(binding.root.context, colorResId)
                binding.itemCalendarEvent.background.setTint(color)
            }

            // Firestore에서 authId를 기반으로 프로필 이미지 가져오기
            loadProfileImage(event.authId!!)

            if(event.startTime != event.endTime){ // 시작날짜와 종료날짜가 같지 않으면 (=하루종일 일정이 아니라면)
                binding.calendarEventTime.visibility = View.VISIBLE
                binding.calendarEventTime.text = "${event.startTime} ~ ${event.endTime}"
            } else{
                binding.calendarEventTime.visibility = View.GONE
            }

            // 아이템 클릭 시 텍스트 입력을 위한 EditText로 변경
            itemView.setOnClickListener {
                startEditing(position)
            }
        }

        private fun startEditing(position: Int) {
            binding.apply {
                calendarEventContent.visibility = View.GONE
                editEventContent.visibility = View.VISIBLE
                editEventContent.requestFocus()

                editEventContent.hint = events[position].title

                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editEventContent, InputMethodManager.SHOW_IMPLICIT)

                // Done 버튼 클릭 시 편집 종료 및 TextView로 전환
                editEventContent.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                        // 편집 종료
                        finishEditing(position, imm)
                        true // 액션 처리 완료
                    } else {
                        false // 액션 처리되지 않음
                    }
                }

                // 포커스가 사라지면 편집 종료
                editEventContent.setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        finishEditing(position, imm)
                    }
                }

//                editEventContent.setOnFocusChangeListener { v, hasFocus ->
//                    if (!hasFocus) {
//                        val newTitle = (v as EditText).text.toString()
//                        events[position].title = newTitle
//                        notifyItemChanged(position)
//                        calendarEventContent.visibility = View.VISIBLE
//                        editEventContent.visibility = View.GONE
//                        imm.hideSoftInputFromWindow(v.windowToken, 0)
//                    }
//                }
            }
        }
        private fun finishEditing(position: Int, imm: InputMethodManager) {
            binding.apply {
                val newTitle = editEventContent.text.toString()
                events[position].title = newTitle.ifEmpty { events[position].title }  // 빈 문자열일 경우 기존 제목 유지
                notifyItemChanged(position)

                // EditText 숨기고 TextView 보여주기
                calendarEventContent.visibility = View.VISIBLE
                editEventContent.visibility = View.GONE

                // 키보드 숨기기
                imm.hideSoftInputFromWindow(editEventContent.windowToken, 0)
            }
        }

        // 일정을 추가한 유저 이미지 가져오기
        private fun loadProfileImage(authId: String) {
            firestore.collection("users").document(authId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val profileImage = document.getString("profileImage")
                        profileImage?.let { imageUrl ->
                            Glide.with(context)
                                .load(imageUrl)
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(R.drawable.profile) // 로딩 중에 보여줄 기본 이미지
                                .error(R.drawable.profile) // 에러 발생 시 보여줄 이미지
                                .into(binding.calendarEventUser) // 이곳에 사용자 프로필 이미지가 들어갈 ImageView의 binding 추가 필요
                        }
                    } else {
                        Log.d("EventRVAdapter", "No such user document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("EventRVAdapter", "Error getting user document: ", exception)
                }
        }
    }
}