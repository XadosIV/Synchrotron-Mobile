package com.dubert.synchrotron.ui.favs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.ArretAdapter
import com.dubert.synchrotron.NextBusActivity
import com.dubert.synchrotron.R
import com.dubert.synchrotron.databinding.FragmentHomeBinding
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class FavsFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var recyclerView : RecyclerView

    private fun getFavData(): ArrayList<String> {
        val arretStorage = context?.let { ArretJSONFileStorage.getInstance(it) }
        val lines = arretStorage!!.getLines()
        val listLines = arrayListOf<Line>()
        val letters = arrayOf('A', 'B', 'C', 'D');
        for (letter in letters) {
            listLines.add(lines.getValue(letter))
        }

        val listFavorites = arrayListOf<String>()
        for (line in listLines) {
            for (arretCode in line.arrets) {
                val arret = arretStorage.findByCode(arretCode)
                if (arret != null) {
                    if (arretCode !in listFavorites && !arret.isOpposite) {
                        if (arret.isFavorite) {
                            listFavorites.add(arretCode)
                        }
                    }
                }
            }
        }
        return listFavorites
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_favs, container, false)

        recyclerView = root.findViewById(R.id.fav_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = ArretAdapter(getFavData(), "favs")
        recyclerView.adapter!!.notifyDataSetChanged()

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}