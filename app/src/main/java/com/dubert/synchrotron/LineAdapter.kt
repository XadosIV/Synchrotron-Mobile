package com.dubert.synchrotron

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LineAdapter (private val linesList : ArrayList<Line>, private val recyclerView: RecyclerView) : RecyclerView.Adapter<LineAdapter.ViewHolder>() {
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
        holder.arretsRecyclerView.isVisible = false
        holder.arretsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.arretsRecyclerView.adapter = ArretAdapter(currentItem.arrets) // TODO : REPLACE WITH LIST FROM DATABASE


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
                onClickListener!!.onClick(holder.arretsRecyclerView, holder.arrow)
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
        fun onClick(view1 : RecyclerView, image : ImageView)
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lineLogo : ImageView = itemView.findViewById(R.id.lineLogo)
        val terminus1Text : TextView = itemView.findViewById(R.id.terminus1Text)
        val terminus2Text : TextView = itemView.findViewById(R.id.terminus2Text)
        val arretsRecyclerView : RecyclerView = itemView.findViewById(R.id.arrets_recycler_view)
        val arrow : ImageView = itemView.findViewById(R.id.button_list)
    }
}