package com.example.z_project.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

    fun updateEvents(newEvents: List<ScheduleModel>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged() // UI 업데이트
    }

    override fun getItemCount(): Int = events.size

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("UNIQUE_CODE", null)
        val event = events[position]

        if (event.authId == userId) {
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
            // 본인 고유코드 가져오기
            sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("UNIQUE_CODE", null)

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


            if(event.authId == userId){
                // 아이템 클릭 시 텍스트 입력을 위한 EditText로 변경
                itemView.setOnClickListener {
                    showEditDialog(event)
                }
            }
        }
        private fun showEditDialog(event: ScheduleModel) {
            // DialogEditEvent 생성 및 표시
            val dialog = DialogEditEvent(context) // context를 DialogEditEvent에 전달
            dialog.show(event) // event를 매개변수로 전달하여 다이얼로그에서 사용할 수 있도록
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