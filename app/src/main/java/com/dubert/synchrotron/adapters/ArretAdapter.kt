package com.dubert.synchrotron.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dubert.synchrotron.R
import com.dubert.synchrotron.activities.NextBusActivity
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage


class ArretAdapter (private val arretsList : ArrayList<String>, private val line : Line) : RecyclerView.Adapter<ArretAdapter.ViewHolder>() {

    val cacheHoraire = HashMap<String, String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_arret, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arretsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arretStorage = ArretJSONFileStorage.getInstance()
        val currentItem = arretStorage.findByCode(arretsList[position])!!

        var text = ""
        if (position == itemCount-1) {
            text = "Terminus"
        }else if (cacheHoraire.containsKey(currentItem.code)){
            text = cacheHoraire[currentItem.code]!!
        }else{
            text = "Récupération des données..."
            val queue = Volley.newRequestQueue(holder.itemView.context)
            val req = StringRequest(Request.Method.GET, "https://live.synchro-bus.fr/"+currentItem.code, {
                val items = currentItem.urlToNextBus(it, line.name)

                if (items.size > 0){
                    text = "Prochain bus à " + items[0].horaire
                }else{
                    text = "Aucun bus prochainement"
                }
                cacheHoraire.set(currentItem.code, text)
                holder.lineText.text = text

            }, {
                Toast.makeText(holder.itemView.context, "La requête n'a pas reçu de réponse...", Toast.LENGTH_LONG).show()
            })
            queue.add(req)
        }
        holder.lineText.text = text


        changeStar(currentItem, holder.favLogo)
        holder.nameArret.text = currentItem.name
        holder.favLogo.setOnClickListener {
            currentItem.isFavorite = !currentItem.isFavorite
            arretStorage.update(currentItem.id, currentItem)
            changeStar(currentItem, holder.favLogo)
        }

        holder.itemView.setOnClickListener {
            val myIntent = Intent(holder.itemView.context, NextBusActivity::class.java)
            myIntent.putExtra("codeArret", arretsList[position]) //Optional parameters
            myIntent.putExtra("line", line.name)
            holder.itemView.context.startActivity(myIntent)
        }
    }

    private fun changeStar(arret : Arret, favLogo : ImageView) {
        if (arret.isFavorite) {
            favLogo.setImageResource((R.drawable.ic_star))
        } else {
            favLogo.setImageResource((R.drawable.ic_star_empty))
        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lineText : TextView = itemView.findViewById(R.id.lineText)
        val nameArret : TextView = itemView.findViewById(R.id.nameArret)
        val favLogo : ImageView = itemView.findViewById(R.id.favLogo)
    }
}