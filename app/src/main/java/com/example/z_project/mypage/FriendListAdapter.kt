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

class CustomAdapter(testDataSet: List<String>, private val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private val localDataSet: List<String> = testDataSet

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById<TextView>(R.id.tv_name)
        val userDelete: ImageView = itemView.findViewById(R.id.iv_x)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = localDataSet[position] // 문자열 값을 직접 설정합니다.
        holder.userDelete.setOnClickListener{
            showListdeleteDialog()
        }
    }

    private fun showListdeleteDialog() {
        ListdeleteFragment(context).show()
    }

    override fun getItemCount(): Int {
        return localDataSet.size
    }
}
