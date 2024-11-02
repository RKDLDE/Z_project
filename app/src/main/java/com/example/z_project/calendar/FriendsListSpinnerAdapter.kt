package com.example.z_project.calendar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.example.z_project.R
import com.example.z_project.databinding.ItemSpinnerYearBinding
import com.example.z_project.mypage.FriendData

class FriendsListAdapter(
    context: Context, @LayoutRes private val resId: Int,
    friendsList: List<FriendData>, private val userName: String,
) : ArrayAdapter<String>(context, resId) {

    private var sortedFriendsList: List<FriendData>
    private var selectedPosition = -1 // 선택된 항목의 위치를 저장할 변수

    init {
        // 로그로 userName과 friendsList 내용을 확인
        Log.d("FriendsListAdapter", "userName: $userName")
        Log.d("FriendsListAdapter", "friendList: $friendsList")
        friendsList.forEach { Log.d("FriendsListAdapter", "Friend: ${it.name}") }

        // "나" 항목을 첫 번째로, 나머지 항목을 가나다순으로 정렬
        val userItem = friendsList.find { it.name.trim() == userName.trim() }
        val otherFriends = friendsList.filter { it.name.trim() != userName.trim() }.sortedBy { it.name }

        Log.d("FriendsListAdapter", "userItem: ${userItem?.name}")
        Log.d("FriendsListAdapter", "otherFriends: ${otherFriends.map { it.name }}")

        // userItem이 null이 아닌 경우에만 리스트에 추가
        sortedFriendsList = listOfNotNull(userItem) + otherFriends

        // 어댑터에 정렬된 리스트의 코드로 초기화
        addAll(sortedFriendsList.map { it.code })
    }

    // 드롭다운하지 않은 상태의 Spinner 항목의 뷰
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemSpinnerYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // 사용자 본인일 경우 "나"로 표시
        binding.itemSpinnerYearTv.text =
            if (sortedFriendsList[position].name == userName) "나"
            else sortedFriendsList[position].name

        Log.d("현재selectedPosition", "$selectedPosition")

        return binding.root
    }

    // 드롭다운된 항목들 리스트의 뷰
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding =
            ItemSpinnerYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.root.background =
            ContextCompat.getDrawable(parent.context, R.drawable.spinner_dropdown_background)

        // 사용자 본인일 경우 "나"로 표시
        binding.itemSpinnerYearTv.text =
            if (sortedFriendsList[position].name == userName) "나"
            else sortedFriendsList[position].name

        return binding.root
    }

    override fun getCount() = sortedFriendsList.size

    override fun getItem(position: Int) = sortedFriendsList[position].toString()

    override fun getItemId(position: Int) = position.toLong()

    // 선택된 항목 설정 메소드
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // 변경사항 적용을 위해 데이터셋을 새로고침
    }
}
