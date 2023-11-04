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

    fun getTerminus(arrets: ArrayList<Arret>): ArrayList<Arret> {
        val list = arrayListOf<Arret>()
        for (arret in arrets){
            Log.i("GETTERMINUS", arret.name + " " + arret.isTerminus)
            if (arret.isTerminus && arret !in list){
                list.add(arret)
            }
        }
        return list
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = linesList[position]

        val arretStorage = ArretJSONFileStorage.getInstance(holder.itemView.context)
        val arretsList = arrayListOf<Arret>()
        val arretsNotOpposite = arrayListOf<String>()
        for (arret in currentItem.arrets) {
            arretStorage.findByCode(arret)?.let {
                arretsList.add(it)
                if (!it.isOpposite && arret !in arretsNotOpposite) { //Pour avoir qu'une seule fois les arrets et pas en double dans l'affichage
                    arretsNotOpposite.add(arret)
                }
            }
        }
        val terminusList = getTerminus(arretsList)

        holder.lineLogo.setImageResource(Line.charToLineLogo(currentItem.name))
        holder.terminus1Text.text = terminusList[0].name

        if (terminusList.size == 2) {
            holder.terminus2Text.text = terminusList[1].name
        } else {
            holder.terminus2Text.text = "PR Maison Brûlée" //TODO : Essayer de fix
            // Encore une fois, le seul terminus non récupéré est celui-ci ??
            // Ca n'a pas de sens, à essayer de fix plus tard, mais pas compris pourquoi
        }


        holder.arretsRecyclerView.setHasFixedSize(true)
        holder.arretsRecyclerView.isVisible = false
        holder.nestedRecyclerView.isVisible = false
        holder.arretsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)

        holder.arretsRecyclerView.adapter = ArretAdapter(arretsNotOpposite, "line")

        // On désactive le défilement du parent quand l'enfant s'apprête à être scroller
        holder.arretsRecyclerView.setOnTouchListener (object:View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean{
                if (v != null) {
                    if (v.id == holder.arretsRecyclerView.id){
                        if (event != null) {
                            if (event.actionMasked == MotionEvent.ACTION_UP){
                                recyclerView.requestDisallowInterceptTouchEvent(false)
                            }else{
                                recyclerView.requestDisallowInterceptTouchEvent(true)
                            }
                        }
                    }
                }

                return true
            }
        })

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(holder.arretsRecyclerView, holder.nestedRecyclerView, holder.arrow)
            }
        }

        buttonArret.setOnClickListener { itemView ->
            if (location != null) {
                val myIntent = Intent(itemView.context, NextBusActivity::class.java)
                myIntent.putExtra("codeArret", findArret(itemView, currentItem)) //Optional parameters
                itemView.context.startActivity(myIntent)
            } else {
                Toast.makeText(itemView.context, "Vous n'avez pas activé la localisation", Toast.LENGTH_LONG).show();
            }
        }
    }

    fun findArret(view: View, line: Line): String {
        val arretStorage = ArretJSONFileStorage.getInstance(view.context)
        val arretsList = HashMap<String, Array<Double>>()
        for (arret in line.arrets) {
            arretStorage.findByCode(arret)?.let {
                arretsList.put(it.code, arrayOf(it.lon.toDouble(), it.lat.toDouble()))
            }
        }
        var arretMin = "Aucun"
        var minDistance = calculDistance(arretsList.iterator().next().value)

        for (arret in arretsList) {
            var dist = calculDistance(arret.value)
            Log.i("NOM", arret.key + " " + dist)
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

    companion object {
        private var onClickListener: OnClickListener? = null
        fun setOnClickListener(onClickListener: OnClickListener) {
            this.onClickListener = onClickListener
        }
    }
    interface OnClickListener {
        fun onClick(view1 : RecyclerView, nested: NestedScrollView, image : ImageView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lineLogo : ImageView = itemView.findViewById(R.id.lineLogo)
        val terminus1Text : TextView = itemView.findViewById(R.id.terminus1Text)
        val terminus2Text : TextView = itemView.findViewById(R.id.terminus2Text)
        val arretsRecyclerView : RecyclerView = itemView.findViewById(R.id.arrets_recycler_view)
        val nestedRecyclerView : NestedScrollView = itemView.findViewById(R.id.nested_recycler_view)
        val arrow : ImageView = itemView.findViewById(R.id.button_list)
    }
}