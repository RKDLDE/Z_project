package com.example.z_project.chatting2.group

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.chatting2.GroupChat
import com.example.z_project.databinding.ItemChatGroupListBinding

class ChatGroupListAdapter(private var items: List<GroupChat>) :
    RecyclerView.Adapter<ChatGroupListAdapter.ViewHolder>() {
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
    ): ChatGroupListAdapter.ViewHolder {
        val binding: ItemChatGroupListBinding = ItemChatGroupListBinding.inflate(
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

    inner class ViewHolder(val binding: ItemChatGroupListBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: GroupChat) {
            if(items.profile.isNullOrEmpty()){
                binding.chatGroupListImage.setImageResource(R.drawable.basic_group_chat_image)
            } else{
                binding.chatGroupListImage.setImageResource(items.profile.toInt()) //그룹 채팅방 이미지
            }
            binding.chatGroupListName.text = items.name //그룹 채팅방 이름
            binding.chatAlarmCount.text = items.alarmCount.toString() //채팅 알람수
        }
    }
}