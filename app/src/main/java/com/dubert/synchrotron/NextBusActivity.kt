package com.dubert.synchrotron

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dubert.synchrotron.storage.ArretJSONFileStorage


class NextBusActivity : AppCompatActivity(R.layout.activity_arret) {

    private lateinit var myAdapter : NextBusAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        val code = b!!.getString("codeArret")!!
        val storage = ArretJSONFileStorage.getInstance()
        var lineName : Char? = null
        if (b.containsKey("line")){
            lineName = b.getChar("line")
        }
        val recyclerview = findViewById<RecyclerView>(R.id.next_bus_recycler_view)

        val arret = storage.findByCode(code)!!
        val arretName = findViewById<TextView>(R.id.arret_name)
        arretName.text = arret.name

        val queue = Volley.newRequestQueue(this)

        val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/"+code, {
            val items = arret.urlToNextBus(it, lineName)

            myAdapter = NextBusAdapter(items)
            recyclerview.adapter = myAdapter;
            recyclerview.layoutManager = LinearLayoutManager(this);

        }, {
        })
        queue.add(req)

        val retour = findViewById<ImageView>(R.id.retour)
        retour.setOnClickListener {
            finish()
        }
    }
}