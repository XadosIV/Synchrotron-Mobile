package com.dubert.synchrotron

import android.content.Context
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
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class ArretAdapter (private val arretsList : ArrayList<String>) : RecyclerView.Adapter<ArretAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_arret, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return arretsList.size
    }

    fun getNextBus(code: String, context: Context): Int { //TODO à faire jsp comment faire
        val queue = Volley.newRequestQueue(context)
        val url = "https://live.synchro-bus.fr/" + code

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
                Response.Listener { response ->
                    Log.i("RESULTAT", response.toString())
            },
                Response.ErrorListener { error ->
                    Toast.makeText(context, "Website didn't respond", Toast.LENGTH_LONG).show();
            }
        )
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arretStorage = ArretJSONFileStorage.getInstance(holder.itemView.context)
        val currentItem = arretStorage.findByCode(arretsList[position])

        holder.lineText.text = "Prochain bus dans : " + getNextBus(arretsList[position], holder.itemView.context) + " minutes"
        if (currentItem != null) {
            holder.nameArret.text = currentItem.name
        }
        if (currentItem != null) {
            if (currentItem.isFavorite) {
                holder.favLogo.setImageResource((R.drawable.ic_star))
            } else {
                holder.favLogo.setImageResource((R.drawable.ic_star_empty))
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val lineLogo : ImageView = itemView.findViewById(R.id.lineLogo)
        val lineText : TextView = itemView.findViewById(R.id.lineText)
        val nameArret : TextView = itemView.findViewById(R.id.nameArret)
        val favLogo : ImageView = itemView.findViewById(R.id.favLogo)
    }
}