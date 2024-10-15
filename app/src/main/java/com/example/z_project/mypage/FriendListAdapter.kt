package com.example.z_project.mypage

import android.content.Context
import android.util.Log
import com.example.z_project.R // 올바른 패키지에서 R을 가져옵니다.
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.z_project.mypage.ListdeleteFragment

class CustomAdapter(private val friendList: MutableList<FriendData>, private val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tv_name)
        val userProfile: ImageView = itemView.findViewById(R.id.iv_profile) // 프로필 이미지
        val userDelete: ImageView = itemView.findViewById(R.id.iv_x)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friendList[position]
        holder.userName.text = friend.name

        Glide.with(context)
            .load(friend.profileImage)
            .apply(RequestOptions.circleCropTransform()) // 비트맵을 둥글게 처리
            .placeholder(R.drawable.profile) // 로딩 중에 보여줄 기본 이미지
            .error(R.drawable.profile) // 에러 발생 시 보여줄 이미지
            .into(holder.userProfile)

        holder.userDelete.setOnClickListener {
            showListdeleteDialog(position)
        }
    }

    private fun showListdeleteDialog(position: Int) {
        val friendCode = friendList[position].code // 친구 코드 가져오기

        ListdeleteFragment(context, object : OnDeleteListener {
            override fun onDelete(pos: Int) {
                // 해당 아이템 삭제 후 어댑터에 반영
                friendList.removeAt(pos)

                // SharedPreferences에서 친구 정보 삭제
                val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                Log.d("Friendlist", "${friendCode} 삭제 예정")

                editor.remove(friendCode) // 친구 코드로 저장된 이름 삭제
                editor.remove("${friendCode}_profileImage") // 프로필 이미지 삭제
                editor.apply()

                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, friendList.size)

                Log.d("Friendlist", "${friendCode} 친구 삭제")
                Toast.makeText(context, "친구가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }, position).show()
    }

    override fun getItemCount(): Int {
        return friendList.size
    }
}
