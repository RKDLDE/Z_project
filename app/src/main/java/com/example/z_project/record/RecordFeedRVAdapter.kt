package com.example.z_project.record

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.z_project.R
import com.example.z_project.databinding.ItemRecordFeedBinding

class RecordFeedRVAdapter(private var items: List<FeedModel>) :
    RecyclerView.Adapter<RecordFeedRVAdapter.ViewHolder>() {
    // 클릭 리스너 정의
    var onItemClick: ((FeedModel) -> Unit)? = null

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
            LayoutInflater.from(parent.context), parent, false
        )
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
        fun bindItems(item: FeedModel) {
            Glide.with(binding.root.context)
                .load(item.uploadImage) // uploadImage는 URL
                .into(binding.itemRecordFeedImage) // ImageView에 로드

            // 프로필 이미지 로드
            loadProfileImage(item.profileImage)
            binding.itemRecordEmoji.text = item.uploadEmoji

            if (item.feedText.isNotEmpty()) {
                binding.itemRecordText.text = item.feedText
                binding.itemRecordText.visibility = View.VISIBLE // Show the TextView
            } else {
                binding.itemRecordText.visibility = View.GONE // Hide the TextView
            }


            // 클릭 리스너 설정
            binding.root.setOnClickListener {
                onItemClick?.invoke(item) // 클릭 시 아이템 전달
            }
        }


        private fun loadProfileImage(url: String) {
            // Glide를 사용하여 프로필 이미지를 둥글게 로드
            Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform()) // 이미지를 둥글게 처리
                .placeholder(R.drawable.profile) // 로딩 중에 기본 이미지 표시
                .error(R.drawable.profile) // 오류 발생 시 기본 이미지 표시
                .into(binding.itemRecordUser) // 프로필 이미지 뷰
        }
    }

    // Adapter의 아이템 업데이트 함수
    fun updateItems(newItems: List<FeedModel>) {
        items = newItems
        notifyDataSetChanged() // 데이터 변경 통지

//    RecyclerView.ViewHolder(binding.root) {
//        fun bindItems(items: FeedModel) {
//            binding.itemRecordFeedImage.setImageResource(items.uploadImage) // 업로드한 메인 이미지
//            binding.itemRecordUser.setImageResource((items.userImage)) // 업로드한 유저의 프로필
//            binding.itemRecordEmoji.setImageResource((items.uploadEmoji)) // 업로드한 내용의 이모티콘
//            binding.itemRecordText.text = items.feedText // 업로드한 내용의 텍스트
//        }
//    }
    }
}
