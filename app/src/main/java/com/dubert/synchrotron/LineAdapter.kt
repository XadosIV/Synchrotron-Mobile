package com.dubert.synchrotron

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LineAdapter (private val linesList : ArrayList<Line>) : RecyclerView.Adapter<LineAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_line, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return linesList.size
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
        val currentItem = linesList[position]
        holder.lineLogo.setImageResource(charToLineLogo(currentItem.name))
        holder.terminus1Text.text = currentItem.terminus1 + " - " // TODO : REPLACE WITH NAME FROM DATABASE
        holder.terminus2Text.text = currentItem.terminus2 // TODO : REPLACE WITH NAME FROM DATABASE

        holder.arretsRecyclerView.setHasFixedSize(true)
        holder.arretsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.arretsRecyclerView.adapter = ArretAdapter(currentItem.arrets) // TODO : REPLACE WITH LIST FROM DATABASE

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(holder.arretsRecyclerView)
                Log.i("EEEEEEEEH", holder.arretsRecyclerView.isVisible.toString())
            }
        }
    }

    companion object {
        private var onClickListener: OnClickListener? = null
        fun setOnClickListener(onClickListener: OnClickListener) {
            this.onClickListener = onClickListener
        }
    }
    interface OnClickListener {
        fun onClick(view1 : RecyclerView)
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lineLogo : ImageView = itemView.findViewById(R.id.lineLogo)
        val terminus1Text : TextView = itemView.findViewById(R.id.terminus1Text)
        val terminus2Text : TextView = itemView.findViewById(R.id.terminus2Text)
        val arretsRecyclerView : RecyclerView = itemView.findViewById(R.id.arrets_recycler_view)
    }
}