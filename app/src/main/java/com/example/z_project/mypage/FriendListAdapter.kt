package com.example.z_project.mypage

import android.content.Context
import com.example.z_project.R // 올바른 패키지에서 R을 가져옵니다.
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.mypage.ListdeleteFragment

class CustomAdapter(testDataSet: MutableList<String>, private val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val localDataSet: MutableList<String> = testDataSet

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tv_name)
        val userDelete: ImageView = itemView.findViewById(R.id.iv_x)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = localDataSet[position]
        holder.userDelete.setOnClickListener {
            showListdeleteDialog(position)
        }
    }

    private fun showListdeleteDialog(position: Int) {
        ListdeleteFragment(context, object : OnDeleteListener {
            override fun onDelete(pos: Int) {
                // 해당 아이템 삭제 후 어댑터에 반영
                localDataSet.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, localDataSet.size)
            }
        }, position).show()
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}
