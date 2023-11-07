package com.dubert.synchrotron

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.NextBus
import com.dubert.synchrotron.storage.ArretJSONFileStorage


class NextBusActivity : AppCompatActivity(R.layout.activity_arret) {

    private lateinit var myAdapter : NextBusAdapter
    private lateinit var items : ArrayList<NextBus>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        var code = b!!.getString("codeArret")!!
        val storage = ArretJSONFileStorage.getInstance()
        var lineName : Char? = null
        var direction = 0
        var pressed = true
        var base : ArrayList<String> = arrayListOf()
        var notBase : ArrayList<String> = arrayListOf()
        if (b.containsKey("line")){
            lineName = b.getChar("line")
            base = storage.getLine(b.getChar("line"))!!.forward
            notBase = storage.getLine(b.getChar("line"))!!.backward
        }
        val recyclerview = findViewById<RecyclerView>(R.id.next_bus_recycler_view)

        val arret = storage.findByCode(code)!!
        val arretName = findViewById<TextView>(R.id.arret_name)
        arretName.text = arret.name

        val noBus = findViewById<TextView>(R.id.noBus)
        noBus.isVisible = false

        val queue = Volley.newRequestQueue(this)

        val see_all_lines = findViewById<Button>(R.id.see_all_lines)
        see_all_lines.setOnClickListener {
            pressed = !pressed
            if (pressed) {
                see_all_lines.setBackgroundColor(Color.argb(255,255,99,71))
                if (b.containsKey("line")){
                    lineName = b.getChar("line")
                }
            } else {
                see_all_lines.setBackgroundColor(Color.argb(255,134, 146, 247))
                lineName = null
            }
            if (direction == 0) {
                val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/" + storage.findByCode(code)!!.opposite, {
                    items = arret.urlToNextBus(it, lineName)
                }, {
                })
                queue.add(req)
            } else {
                val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/" + code, {
                    items = arret.urlToNextBus(it, lineName)
                }, {
                })
                queue.add(req)
            }
            recyclerview.adapter = NextBusAdapter(items)
            noBus.isVisible = items.size == 0
        }

        val button_change_direction = findViewById<Button>(R.id.change_direction_button)
        if (lineName == null) {
            see_all_lines.isVisible = false
            button_change_direction.isVisible = false
        } else {
            if (code !in base) {
                base = storage.getLine(b.getChar("line"))!!.backward
                notBase = storage.getLine(b.getChar("line"))!!.forward
            }
            button_change_direction.text = "Direction : " + storage.findByCode(base.get(0))!!.name.uppercase()
        }
        button_change_direction.setOnClickListener {
            if (direction == 0) {
                direction = 1
                button_change_direction.text = "Direction : " + storage.findByCode(notBase.get(0))!!.name.uppercase()
                val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/"+ storage.findByCode(code)!!.opposite, {
                    items = arret.urlToNextBus(it, lineName)
                }, {
                })
                queue.add(req)
            } else {
                direction = 0
                button_change_direction.text = "Direction : " + storage.findByCode(base.get(0))!!.name.uppercase()
                val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/"+code, {
                    items = arret.urlToNextBus(it, lineName)
                }, {
                })
                queue.add(req)
            }
            recyclerview.adapter = NextBusAdapter(items)
            noBus.isVisible = items.size == 0
        }

        val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/"+code, {
            items = arret.urlToNextBus(it, lineName)
            noBus.isVisible = items.size == 0

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