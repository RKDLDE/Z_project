package com.example.z_project.chatting2.personal

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.chatting2.PersonalChat
import com.example.z_project.databinding.ItemChatPersonalListBinding

class ChatPersonalListAdapter (private var items: List<PersonalChat>) :
    RecyclerView.Adapter<ChatPersonalListAdapter.ViewHolder>() {
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private lateinit var itemClickListener: onItemClickListener

    fun setItemClickListener(itemClickListener: onItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatPersonalListAdapter.ViewHolder {
        val binding: ItemChatPersonalListBinding = ItemChatPersonalListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(val binding: ItemChatPersonalListBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: PersonalChat) {
            if(items.profile.isNullOrEmpty()){
                binding.chatPersonalListUserImage.setImageResource(R.drawable.profile)
            } else{
                binding.chatPersonalListUserImage.setImageResource(items.profile.toInt()) //그룹 채팅방 이미지
            }
            binding.chatPersonalListUserName.text = items.name //채팅방 이름
            binding.chatPersonalListContent.text = items.content //채팅 내용 (가장 최근)
            binding.chatAlarmCount.text = items.alarmCount.toString() //채팅 알람수
        }
    }
}