package com.dubert.synchrotron

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class LineAdapter (private val linesList : ArrayList<Line>, private val recyclerView: RecyclerView) : RecyclerView.Adapter<LineAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_line, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return linesList.size
    }

    fun getTerminus(arrets: ArrayList<Arret>): ArrayList<Arret> {
        val list = arrayListOf<Arret>()
        for (arret in arrets){
            if (arret.isTerminus){
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
        holder.terminus1Text.text = terminusList[0].name + " - "
        if (terminusList.size == 2) {
            holder.terminus2Text.text = terminusList[1].name
        } else {
            holder.terminus2Text.text = "PR Maison Brûlée" //TODO : Essayer de fix
            // Encore une fois, le seul terminus non récupéré est celui-ci ??
            // Ca n'a pas de sens, à essayer de fix plus tard, mais pas compris pourquoi
        }

        holder.arretsRecyclerView.setHasFixedSize(true)
        holder.arretsRecyclerView.isVisible = false
        holder.arretsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.arretsRecyclerView.adapter = ArretAdapter(arretsNotOpposite) // TODO : REPLACE WITH LIST FROM DATABASE


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