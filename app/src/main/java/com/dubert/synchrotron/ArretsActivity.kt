package com.dubert.synchrotron

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class ArretsActivity: AppCompatActivity(R.layout.activity_arrets_list) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        val arretsList = b!!.getStringArrayList("arretsList")
        val arretsListOpposite = b!!.getStringArrayList("arretsListOpposite")
        val lineLogo = b!!.getInt("lineLogo")
        var direction = 0

        val storage = ArretJSONFileStorage.getInstance()

        val arretsRecyclerView = findViewById<RecyclerView>(R.id.arrets_recycler_view)
        val change = findViewById<Button>(R.id.change_direction_button)

        arretsRecyclerView.setHasFixedSize(true)
        arretsRecyclerView.layoutManager = LinearLayoutManager(this)
        arretsRecyclerView.adapter = arretsList?.let { ArretAdapter(it, "line") }

        val logo = findViewById<ImageView>(R.id.lineLogo)
        logo.setImageResource(lineLogo)

        val retour = findViewById<ImageView>(R.id.retour)
        retour.setOnClickListener {
            finish()
        }

        change.text = "Direction : " + storage.findByCode(arretsListOpposite!!.get(0))!!.name.uppercase()
        change.setOnClickListener {
            if (direction == 0) {
                direction = 1
                change.text = "Direction : " + storage.findByCode(arretsList!!.get(0))!!.name.uppercase()
                arretsRecyclerView.adapter = ArretAdapter(arretsListOpposite, "line")
            } else {
                direction = 0
                change.text = "Direction : " + storage.findByCode(arretsListOpposite!!.get(0))!!.name.uppercase()
                arretsRecyclerView.adapter = ArretAdapter(arretsList!!, "line")
            }
        }
    }
}