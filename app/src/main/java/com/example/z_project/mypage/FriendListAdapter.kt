package com.example.z_project.mypage

import com.example.z_project.R // 올바른 패키지에서 R을 가져옵니다.
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(testDataSet: List<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private val localDataSet: List<String> = testDataSet

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById<TextView>(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = localDataSet[position] // 문자열 값을 직접 설정합니다.
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}
