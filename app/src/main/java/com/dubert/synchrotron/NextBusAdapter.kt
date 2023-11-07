package com.dubert.synchrotron

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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.model.NextBus
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class NextBusAdapter (private val nextBusList : ArrayList<NextBus>) : RecyclerView.Adapter<NextBusAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_next_bus, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return nextBusList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bus = nextBusList[position]

        holder.terminusText.text = bus.direction
        holder.timeText.text = bus.horaire
        holder.logo.setImageResource(Line.charToLineLogo(bus.line))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val terminusText : TextView = itemView.findViewById(R.id.terminusText)
        val timeText : TextView = itemView.findViewById(R.id.timeText)
        val logo : ImageView = itemView.findViewById(R.id.lineLogo)
    }
}