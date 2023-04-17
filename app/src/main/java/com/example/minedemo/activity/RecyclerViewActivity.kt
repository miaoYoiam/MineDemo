package com.example.minedemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.lib_annotation.PagePath
import com.example.lib_annotation.PageType
import com.example.minedemo.R
import com.example.minedemo.UserAdapter
import com.example.minedemo.UserDiffCallback
import com.example.minedemo.widget.RecyclerListView
import kotlinx.android.synthetic.main.activity_recycler_view.*

@PagePath(id = "recycler", type = PageType.IFragment, allowMultiMode = true)
class RecyclerViewActivity : AppCompatActivity(), RecyclerListView.OnItemTouchMoveListener {
    private val userAdapter = UserAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        initView()
        initData()
    }

    private fun initView() {
        recyclerView.run {
            layoutManager = LinearLayoutManager(this@RecyclerViewActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = userAdapter
            setOnItemLongClickListener(this@RecyclerViewActivity)
        }

    }

    private fun initData() {
        val data = mutableListOf<Int>()
        data.add(0)
        data.add(1)
        data.add(2)
        data.add(3)
        data.add(4)
        data.add(5)
        data.add(6)
        data.add(7)
        data.add(8)
        data.add(9)
        data.add(0)
        data.add(1)
        data.add(2)
        data.add(3)
        data.add(4)
        data.add(5)
        data.add(6)
        data.add(7)
        data.add(8)
        data.add(9)
        data.add(0)
        data.add(1)
        data.add(2)
        data.add(3)
        data.add(4)
        data.add(5)
        data.add(6)
        data.add(7)
        data.add(8)
        data.add(9)
        userAdapter.dataList.addAll(data)
        userAdapter.notifyDataSetChanged()
    }

    private fun diffData() {
        val origin = mutableListOf<Int>()
        origin.addAll(userAdapter.dataList)

        userAdapter.dataList.add(11)
        userAdapter.dataList.add(3)
        userAdapter.dataList.add(12)
        userAdapter.dataList.add(1)
        userAdapter.dataList.add(13)
        userAdapter.dataList.add(2)
        userAdapter.dataList.add(14)


        val diffCallback = UserDiffCallback(origin, userAdapter.dataList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                userAdapter.notifyItemRangeChanged(position, count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                userAdapter.notifyItemMoved(fromPosition, toPosition)
            }

            override fun onInserted(position: Int, count: Int) {
                userAdapter.notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                userAdapter.notifyItemRangeRemoved(position, count)
            }
        })
    }

    override fun onMove(view: View?, position: Int, dx: Float, dy: Float) {
        println("----> ${view.hashCode()}  ${position}  $dx $dy")
    }

}