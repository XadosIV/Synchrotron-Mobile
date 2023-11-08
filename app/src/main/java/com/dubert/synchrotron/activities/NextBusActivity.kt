package com.dubert.synchrotron.activities

import android.annotation.SuppressLint
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
import com.dubert.synchrotron.R
import com.dubert.synchrotron.adapters.NextBusAdapter
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.model.NextBus
import com.dubert.synchrotron.storage.ArretJSONFileStorage


class NextBusActivity : AppCompatActivity(R.layout.activity_arret) {

    private lateinit var myAdapter : NextBusAdapter
    private lateinit var items : ArrayList<NextBus>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        val code = b!!.getString("codeArret")!!
        val storage = ArretJSONFileStorage.getInstance()
        var lineParam : Char? = null
        var direction = 0
        var pressed = false
        var base : ArrayList<String>? = null
        var notBase : ArrayList<String>? = null
        var line : Line? = null
        if (b.containsKey("line")) {
            lineParam = b.getChar("line")
            line = storage.getLine(lineParam)
        }
        val recyclerview = findViewById<RecyclerView>(R.id.next_bus_recycler_view)

        val arret = storage.findByCode(code)!!
        val arretName = findViewById<TextView>(R.id.arret_name)
        arretName.text = arret.name

        val noBus = findViewById<TextView>(R.id.noBus)
        noBus.isVisible = false

        val queue = Volley.newRequestQueue(this)

        val see_all_lines = findViewById<Button>(R.id.see_all_lines)
        if (lineParam != null){
            see_all_lines.isVisible = true
            see_all_lines.setOnClickListener {
                pressed = !pressed
                if (!pressed) {
                    see_all_lines.setBackgroundColor(Color.argb(255,255,99,71))
                    if (b.containsKey("line")){
                        lineParam = b.getChar("line")
                    }
                } else {
                    see_all_lines.setBackgroundColor(Color.argb(255,134, 146, 247))
                    lineParam = null
                }
                if (direction == 0) {
                    val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/" + arret.code, {
                        items = arret.urlToNextBus(it, lineParam)
                        recyclerview.adapter = NextBusAdapter(items)
                        noBus.isVisible = items.size == 0
                    }, {
                    })
                    queue.add(req)
                } else {
                    val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/" + arret.opposite, {
                        items = arret.urlToNextBus(it, lineParam)
                        recyclerview.adapter = NextBusAdapter(items)
                        noBus.isVisible = items.size == 0
                    }, {
                        Toast.makeText(this, "La requête n'a pas reçu de réponse...", Toast.LENGTH_LONG).show()
                    })
                    queue.add(req)
                }
            }
        }else{
            see_all_lines.isVisible = false
        }


        val button_change_direction = findViewById<Button>(R.id.change_direction_button)

        button_change_direction.isVisible = true
        if (line != null){
            if (line.forward.contains(arret.code)) {
                base = line.forward
                notBase = line.backward
            }else{
                base = line.backward
                notBase = line.forward
            }
            button_change_direction.text = "Direction : " + storage.findByCode(base[base.size-1])!!.name.uppercase()
        }else{
            button_change_direction.text = "Changer de sens"
        }
        button_change_direction.setOnClickListener {
            if (direction == 0) {
                direction = 1
                if (notBase != null) button_change_direction.text = "Direction : " + storage.findByCode(notBase[notBase.size-1])!!.name.uppercase()
                val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/"+ arret.opposite, {
                    items = arret.urlToNextBus(it, lineParam)
                    recyclerview.adapter = NextBusAdapter(items)
                    noBus.isVisible = items.size == 0
                }, {
                    Toast.makeText(this, "La requête n'a pas reçu de réponse...", Toast.LENGTH_LONG).show()
                })
                queue.add(req)
            } else {
                direction = 0
                if (base != null) button_change_direction.text = "Direction : " + storage.findByCode(base[base.size-1])!!.name.uppercase()
                val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/"+arret.code, {
                    items = arret.urlToNextBus(it, lineParam)
                    recyclerview.adapter = NextBusAdapter(items)
                    noBus.isVisible = items.size == 0
                }, {
                    Toast.makeText(this, "La requête n'a pas reçu de réponse...", Toast.LENGTH_LONG).show()
                })
                queue.add(req)
            }
        }




        val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/"+arret.code, {
            items = arret.urlToNextBus(it, lineParam)
            noBus.isVisible = items.size == 0

            myAdapter = NextBusAdapter(items)
            recyclerview.adapter = myAdapter;
            recyclerview.layoutManager = LinearLayoutManager(this);

        }, {
            Toast.makeText(this, "La requête n'a pas reçu de réponse...", Toast.LENGTH_LONG).show()
        })
        queue.add(req)

        val retour = findViewById<ImageView>(R.id.retour)
        retour.setOnClickListener {
            finish()
        }
    }
}