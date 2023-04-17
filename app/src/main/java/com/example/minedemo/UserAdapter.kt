package com.example.minedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var dataList = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserHolder).refresh(dataList[position])
    }


    inner class UserHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun refresh(data: Int) {
            itemView.run {
                content.text = data.toString()
                setOnClickListener {
                    println("啊啊哈哈哈哈哈哈哈")
                }
            }
        }
    }

}