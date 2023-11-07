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
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var searchView: SearchView
    private lateinit var dataList: ArrayList<Arret>
    private lateinit var searchList: ArrayList<String>

    private lateinit var recyclerView : RecyclerView
    private val fragment = this

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getArretData(): ArrayList<Arret> {
        val arretStorage = ArretJSONFileStorage.getInstance()
        val listLines = arrayListOf<Line>()
        val letters = arrayOf('A', 'B', 'C', 'D');
        for (letter in letters) {
            arretStorage!!.getLine(letter)?.let { listLines.add(it) }
        }

        val list = arrayListOf<Arret>()
        for (line in listLines) {
            for (arret in line.forward) {
                if (arretStorage != null) {
                    arretStorage.findByCode(arret)?.let {
                        if (it !in list) {
                            list.add(it)
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
        val storage = ArretJSONFileStorage.getInstance()
        recyclerView.adapter = ArretAdapter(arrayListOf(), storage.getLine('A')!!, "home")

        dataList = getArretData()
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
                        if (it.name.lowercase(Locale.getDefault()).startsWith(searchText)) {
                            searchList.add(it.code)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                recyclerView.adapter = ArretAdapter(searchList, storage.getLine('A')!!, "home")
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
