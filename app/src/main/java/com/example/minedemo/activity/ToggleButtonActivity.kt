package com.example.minedemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.minedemo.R
import kotlinx.android.synthetic.main.activity_toggle_button.*

class ToggleButtonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toggle_button)
        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->  }
    }

    fun onclick(view: View) {
        toggleButton.toggle()
    }
}