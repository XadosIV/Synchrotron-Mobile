package com.dubert.synchrotron.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.ArretAdapter
import com.dubert.synchrotron.R
import com.dubert.synchrotron.databinding.FragmentHomeBinding
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var searchView: SearchView
    private lateinit var dataList: ArrayList<String>
    private lateinit var searchList: ArrayList<String>

    private lateinit var recyclerView : RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getArretData(): ArrayList<String> {
        val arretStorage = context?.let { ArretJSONFileStorage.getInstance(it) }
        val listLines = arrayListOf<Line>()
        val letters = arrayOf('A', 'B', 'C', 'D');
        for (letter in letters) {
            arretStorage!!.getLine(letter)?.let { listLines.add(it) }
        }

        val list = arrayListOf<String>()
        for (line in listLines) {
            for (arret in line.arrets) {
                if (arretStorage != null) {
                    arretStorage.findByCode(arret)?.let {
                        if (arret !in list && !(it.isOpposite)) {
                            list.add(arret)
                        }
                    }
                }
            }
        }
        return list
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

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
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    dataList.forEach{
                        if (it.lowercase(Locale.getDefault()).startsWith(searchText)) {
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
