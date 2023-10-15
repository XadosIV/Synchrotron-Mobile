package com.dubert.synchrotron.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.Arret
import com.dubert.synchrotron.ArretAdapter
import com.dubert.synchrotron.R
import com.dubert.synchrotron.databinding.FragmentHomeBinding
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var searchView: SearchView
    private lateinit var dataList: ArrayList<Arret>
    private lateinit var searchList: ArrayList<Arret>

    private lateinit var historiqueList : ArrayList<Arret>
    private lateinit var historiqueText : TextView

    private lateinit var recyclerView : RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getArretData(): ArrayList<Arret> {
        val list = arrayListOf<Arret>()

        list.add(Arret('A', "UJACO1"))
        list.add(Arret('B', "ROCNO1"))
        list.add(Arret('C', "DEGAC1"))
        list.add(Arret('D', "PLASP1"))

        return list
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        historiqueList = arrayListOf() // TODO : REPLACE WITH LIST FROM DATABASE
        historiqueText = root.findViewById(R.id.historique_text)
        if (historiqueList.isEmpty()) {
            historiqueText.text = ""
        } else {
            historiqueText.text = "Historique"
        }

        recyclerView = root.findViewById(R.id.arrets_recycler_view)
        searchView = root.findViewById(R.id.search_home)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ArretAdapter(arrayListOf())

        dataList = getArretData() // TODO : REPLACE WITH LIST FROM DATABASE
        searchList = arrayListOf()

        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    dataList.forEach{
                        if (it.code.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                recyclerView.adapter = ArretAdapter(searchList)
                return false
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
