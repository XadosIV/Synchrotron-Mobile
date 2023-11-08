package com.dubert.synchrotron.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.adapters.ArretAdapter
import com.dubert.synchrotron.R
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class ArretsActivity: AppCompatActivity(R.layout.activity_arrets_list) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        var direction = 0

        val storage = ArretJSONFileStorage.getInstance()
        val line = storage.getLine(b!!.getChar("line"))!!

        val arretsRecyclerView = findViewById<RecyclerView>(R.id.arrets_recycler_view)
        val change = findViewById<Button>(R.id.change_direction_button)

        arretsRecyclerView.setHasFixedSize(true)
        arretsRecyclerView.layoutManager = LinearLayoutManager(this)
        arretsRecyclerView.adapter = ArretAdapter(line.forward, line)

        val logo = findViewById<ImageView>(R.id.lineLogo)
        logo.setImageResource(line.logo())

        val retour = findViewById<ImageView>(R.id.retour)
        retour.setOnClickListener {
            finish()
        }

        change.text = "Direction : " + storage.findByCode(line.backward.get(0))!!.name.uppercase()
        change.setOnClickListener {
            if (direction == 0) {
                direction = 1
                change.text = "Direction : " + storage.findByCode(line.forward.get(0))!!.name.uppercase()
                arretsRecyclerView.adapter = ArretAdapter(line.backward, line)
            } else {
                direction = 0
                change.text = "Direction : " + storage.findByCode(line.backward.get(0))!!.name.uppercase()
                arretsRecyclerView.adapter = ArretAdapter(line.forward, line)
            }
        }
    }
}