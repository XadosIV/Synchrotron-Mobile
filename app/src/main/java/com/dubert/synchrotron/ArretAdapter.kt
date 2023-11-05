package com.dubert.synchrotron

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.storage.ArretJSONFileStorage


class ArretAdapter (private val arretsList : ArrayList<String>, private val fragment: String) : RecyclerView.Adapter<ArretAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_arret, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arretsList.size
    }

    fun getNextBus(code: String, context: Context): Int {
        ContentScrapper.getHTMLData(MainActivity(),"https://live.synchro-bus.fr/" + code ,object : ContentScrapper.ScrapListener{
            override fun onResponse(html: String?) {
                if(html != null) {
                    //TODO: RECUPERE LE PROCHAIN BUS
                } else {
                    Toast.makeText(context,"Not found",Toast.LENGTH_LONG).show()
                }
            }
        })
        return 0 //TODO: CHANGE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arretStorage = ArretJSONFileStorage.getInstance()
        val currentItem = arretStorage.findByCode(arretsList[position])
        holder.lineText.text = "Prochain bus dans : " + getNextBus(arretsList[position], holder.itemView.context) + " minutes"
        if (currentItem != null) {
            changeStar(currentItem, holder.favLogo)
            holder.nameArret.text = currentItem.name
            holder.favLogo.setOnClickListener {
                currentItem.isFavorite = !currentItem.isFavorite
                arretStorage.update(currentItem.id, currentItem)
                changeStar(currentItem, holder.favLogo)
                if (fragment == "favs") {
                    (holder.itemView.context as Activity).recreate();
                }
            }
        }

        holder.itemView.setOnClickListener {
            val myIntent = Intent(holder.itemView.context, NextBusActivity::class.java)
            myIntent.putExtra("codeArret", arretsList[position]) //Optional parameters
            holder.itemView.context.startActivity(myIntent)
        }
    }

    fun changeStar(arret : Arret, favLogo : ImageView) {
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