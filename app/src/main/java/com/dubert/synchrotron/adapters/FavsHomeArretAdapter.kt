package com.dubert.synchrotron.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.R
import com.dubert.synchrotron.activities.NextBusActivity
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.storage.ArretJSONFileStorage


class FavsHomeArretAdapter (private val arretsList : ArrayList<String>, private val fragment: String) : RecyclerView.Adapter<FavsHomeArretAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_arret_favs_home, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arretsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arretStorage = ArretJSONFileStorage.getInstance()
        val currentItem = arretStorage.findByCode(arretsList[position])!!

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

        holder.itemView.setOnClickListener {
            val myIntent = Intent(holder.itemView.context, NextBusActivity::class.java)
            myIntent.putExtra("codeArret", arretsList[position]) //Optional parameters
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
        val nameArret : TextView = itemView.findViewById(R.id.nameArret)
        val favLogo : ImageView = itemView.findViewById(R.id.favLogo)
    }
}