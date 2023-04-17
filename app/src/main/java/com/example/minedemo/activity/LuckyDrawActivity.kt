package com.example.minedemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minedemo.R
import com.example.minedemo.widget.LuckyDrawWidget
import kotlinx.android.synthetic.main.activity_lucky_draw.*

class LuckyDrawActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lucky_draw)
        initUI()
    }

    private fun initUI() {
        (lucky_draw_widget as LuckyDrawWidget).setData()

        start_btn.setOnClickListener {
            (lucky_draw_widget as LuckyDrawWidget).startLuckDraw()
        }
    }
}