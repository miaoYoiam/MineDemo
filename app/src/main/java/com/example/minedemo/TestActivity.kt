package com.example.minedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.minedemo.activity.MainActivity2
import com.example.minedemo.data.Text
import com.example.minedemo.data.TextList
import com.example.minedemo.data.TextUser
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val list = mutableListOf<TextList>()
        list.add(TextList(TextUser("daisoo")))
        list.add(TextList(null, ""))
        list.add(TextList(null, ""))
        rect.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java).apply {

                putExtra("Test", Text("hdahahha").apply {
                    textList = list
                })
            }
            startActivity(intent)
        }

        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.item_spinner_style,
            resources.getStringArray(R.array.planets_array)
        )
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(R.layout.item_spinner)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        println(" $parent")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println(" $position ")
    }
}