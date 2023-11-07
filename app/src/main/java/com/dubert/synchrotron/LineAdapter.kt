package com.dubert.synchrotron

import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage
import java.lang.Math.pow
import java.lang.Math.sqrt


class LineAdapter(private val linesList: ArrayList<Line>, private val recyclerView: RecyclerView, private val location: Location?) : RecyclerView.Adapter<LineAdapter.ViewHolder>() {

    private lateinit var buttonArret : Button

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_line, parent, false)
        buttonArret = itemView.findViewById(R.id.arret_button)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return linesList.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLine = linesList[position]

        val terminusList = currentLine.getTerminus()

        Log.i("TERMINUS"+position+"FINAL", terminusList[0].name)
        Log.i("TERMINUS"+position+"FINAL", terminusList[1].name)

        holder.lineLogo.setImageResource(Line.charToLineLogo(currentLine.name))
        holder.terminus1Text.text = terminusList[0].name
        holder.terminus2Text.text = terminusList[1].name

        holder.itemView.setOnClickListener {
            val myIntent = Intent(holder.itemView.context, ArretsActivity::class.java)
            myIntent.putExtra("line", currentLine.name) //Optional parameters
            holder.itemView.context.startActivity(myIntent)
        }

        buttonArret.setOnClickListener { itemView ->
            if (location != null) {
                val myIntent = Intent(itemView.context, NextBusActivity::class.java)
                myIntent.putExtra("codeArret", findArret(itemView, currentLine)) //Optional parameters
                myIntent.putExtra("line", currentLine.name) //Optional parameters
                itemView.context.startActivity(myIntent)
            } else {
                Toast.makeText(itemView.context, "Vous n'avez pas activé la localisation", Toast.LENGTH_LONG).show();
            }
        }
    }

    fun findArret(view: View, line: Line): String {
        val arretStorage = ArretJSONFileStorage.getInstance()
        val arretsList = HashMap<String, Array<Double>>()
        for (arret in line.forward) {
            arretStorage.findByCode(arret)?.let {
                arretsList.put(it.code, arrayOf(it.lon.toDouble(), it.lat.toDouble()))
            }
        }
        var minDistance = calculDistance(arretsList.iterator().next().value)
        var arretMin = arretsList.iterator().next().key

        for (arret in arretsList) {
            var dist = calculDistance(arret.value)
            if (dist != null) {
                if (dist < minDistance!!) {
                    minDistance = dist
                    arretMin = arret.key
                }
            }
        }
        return arretMin
    }

    fun calculDistance(value: Array<Double>): Double? {
        val lon = location?.longitude
        val lat = location?.latitude

        return sqrt( pow((lon!! - value[0]),2.0) + pow((lat!! - value[1]),2.0) )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lineLogo : ImageView = itemView.findViewById(R.id.lineLogo)
        val terminus1Text : TextView = itemView.findViewById(R.id.terminus1Text)
        val terminus2Text : TextView = itemView.findViewById(R.id.terminus2Text)
    }
}