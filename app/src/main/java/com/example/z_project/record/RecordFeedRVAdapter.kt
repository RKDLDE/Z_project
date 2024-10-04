package com.example.z_project.record

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.databinding.ItemRecordFeedBinding

class RecordFeedRVAdapter(private var items: List<FeedModel>) :
    RecyclerView.Adapter<RecordFeedRVAdapter.ViewHolder>() {
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
    ): RecordFeedRVAdapter.ViewHolder {
        val binding: ItemRecordFeedBinding = ItemRecordFeedBinding.inflate(
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

    inner class ViewHolder(val binding: ItemRecordFeedBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
            fun bindItems(items: FeedModel) {
                binding.itemRecordFeedImage.setImageResource(items.uploadImage) // 업로드한 메인 이미지
                binding.itemRecordUser.setImageResource((items.userImage)) // 업로드한 유저의 프로필
                binding.itemRecordEmoji.setImageResource((items.uploadEmoji)) // 업로드한 내용의 이모티콘
                binding.itemRecordText.text = items.feedText // 업로드한 내용의 텍스트
            }
        }
}