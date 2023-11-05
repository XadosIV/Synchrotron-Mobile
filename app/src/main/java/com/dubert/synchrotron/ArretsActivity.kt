package com.dubert.synchrotron

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.model.Line

class ArretsActivity: AppCompatActivity(R.layout.activity_arrets_list) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        val arretsList = b!!.getStringArrayList("arretsList")
        val lineLogo = b!!.getInt("lineLogo")

        val arretsRecyclerView = findViewById<RecyclerView>(R.id.arrets_recycler_view)

        arretsRecyclerView.setHasFixedSize(true)
        arretsRecyclerView.layoutManager = LinearLayoutManager(this)

        arretsRecyclerView.adapter = arretsList?.let { ArretAdapter(it, "line") }

        val logo = findViewById<ImageView>(R.id.lineLogo)
        logo.setImageResource(lineLogo)

        val retour = findViewById<ImageView>(R.id.retour)
        retour.setOnClickListener {
            finish()
        }
    }
}