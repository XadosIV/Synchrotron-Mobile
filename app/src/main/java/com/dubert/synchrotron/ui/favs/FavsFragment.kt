package com.dubert.synchrotron.ui.favs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.Arret
import com.dubert.synchrotron.ArretAdapter
import com.dubert.synchrotron.R
import com.dubert.synchrotron.databinding.FragmentHomeBinding

class FavsFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var recyclerView : RecyclerView

    private fun getFavData(): ArrayList<Arret> {
        val list = arrayListOf<Arret>()

        list.add(Arret('A', "BOISS1"))
        list.add(Arret('B', "DUCS2"))
        list.add(Arret('D', "DUCS1"))
        list.add(Arret('C', "CHALL2"))

        return list
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_favs, container, false)

        recyclerView = root.findViewById(R.id.fav_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = ArretAdapter(getFavData())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}