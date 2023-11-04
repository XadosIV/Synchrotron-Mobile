package com.dubert.synchrotron

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class ArretsActivity: AppCompatActivity(R.layout.activity_arrets_list) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        val arretsList = b!!.getStringArrayList("arretsList")

        val arretsRecyclerView = findViewById<RecyclerView>(R.id.arrets_recycler_view)

        arretsRecyclerView.setHasFixedSize(true)
        arretsRecyclerView.layoutManager = LinearLayoutManager(this)

        arretsRecyclerView.adapter = arretsList?.let { ArretAdapter(it, "line") }
    }
}