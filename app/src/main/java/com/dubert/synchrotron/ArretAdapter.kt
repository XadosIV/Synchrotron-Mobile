package com.dubert.synchrotron

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArretAdapter (private val arretsList : ArrayList<Arret>) : RecyclerView.Adapter<ArretAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_arret, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arretsList.size
    }

    fun charToLineLogo(line : Char): Int {
        return when (line.uppercaseChar()){
            'A' -> R.drawable.line_a
            'B' -> R.drawable.line_b
            'C' -> R.drawable.line_c
            'D' -> R.drawable.line_d
            else -> R.drawable.ic_star //debug case
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arretsList[position]
        holder.lineLogo.setImageResource(charToLineLogo(currentItem.line))
        holder.lineText.text = "Ligne : " + currentItem.line.uppercase()
        holder.nameArret.text = currentItem.code // TODO : REPLACE WITH NAME FROM DATABASE
        holder.favLogo.setImageResource((R.drawable.ic_star)) // TODO : REPLACE WITH CHECK IF IS FAVORITE OR NOT
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lineLogo : ImageView = itemView.findViewById(R.id.lineLogo)
        val lineText : TextView = itemView.findViewById(R.id.lineText)
        val nameArret : TextView = itemView.findViewById(R.id.nameArret)
        val favLogo : ImageView = itemView.findViewById(R.id.favLogo)
    }
}