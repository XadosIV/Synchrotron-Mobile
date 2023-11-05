package com.dubert.synchrotron

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.storage.ArretJSONFileStorage


class NextBusActivity : AppCompatActivity(R.layout.activity_arret) {

    private lateinit var myAdapter : NextBusAdapter

    fun getBusList(code: String): Int { //TODO: Change return + découper le HTML
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        val codeArret = b!!.getString("codeArret")
        val arretStorage = ArretJSONFileStorage.getInstance()
        val arret = codeArret?.let { arretStorage.findByCode(it) }

        val recyclerview = findViewById<RecyclerView>(R.id.next_bus_recycler_view)

        val arretName = findViewById<TextView>(R.id.arret_name)
        if (arret != null) {
            arretName.text = arret.name
        }

        ContentScrapper.getHTMLData(this,"https://live.synchro-bus.fr/" + codeArret ,object : ContentScrapper.ScrapListener{
            override fun onResponse(html: String?) {
                if(html != null) {
                    val listBus = getBusList(html)
                } else {
                    Toast.makeText(this@NextBusActivity,"Not found",Toast.LENGTH_LONG).show()
                }
            }
        })

        myAdapter = codeArret?.let { NextBusAdapter(it) }!!
        recyclerview.setAdapter(myAdapter);
        recyclerview.setLayoutManager(LinearLayoutManager(this));

        val retour = findViewById<ImageView>(R.id.retour)
        retour.setOnClickListener {
            finish()
        }
    }
}