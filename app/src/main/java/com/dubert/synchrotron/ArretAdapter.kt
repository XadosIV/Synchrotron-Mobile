package com.dubert.synchrotron

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line

class ArretAdapter (private val arretsList : ArrayList<String>) : RecyclerView.Adapter<ArretAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_arret, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arretsList.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val currentItem = arretsList[position]
        //holder.lineLogo.setImageResource(Line.charToLineLogo('A'))
        holder.lineText.text = "Prochain bus dans :  minutes"
        holder.nameArret.text = "caca" //currentItem.code // TODO : REPLACE WITH NAME FROM DATABASE
        holder.favLogo.setImageResource((R.drawable.ic_star)) // TODO : REPLACE WITH CHECK IF IS FAVORITE OR NOT
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lineLogo : ImageView = itemView.findViewById(R.id.lineLogo)
        val lineText : TextView = itemView.findViewById(R.id.lineText)
        val nameArret : TextView = itemView.findViewById(R.id.nameArret)
        val favLogo : ImageView = itemView.findViewById(R.id.favLogo)
    }
}