package com.dubert.synchrotron.ui.lines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.Arret
import com.dubert.synchrotron.Line
import com.dubert.synchrotron.LineAdapter
import com.dubert.synchrotron.R
import com.dubert.synchrotron.databinding.FragmentHomeBinding

class LinesFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var recyclerView : RecyclerView

    private fun getArretsData(line : Char): ArrayList<Arret> {
        val list = arrayListOf<Arret>()

        list.add(Arret(line, "UJACO1"))
        list.add(Arret(line, "ROCNO1"))
        list.add(Arret(line, "DEGAC1"))
        list.add(Arret(line, "PLASP1"))

        return list
    }
    private fun getLinesData(): ArrayList<Line> {
        val list = arrayListOf<Line>()

        list.add(Line('A', "BOISS1", "BOISS2", getArretsData('A')))
        list.add(Line('B', "DUCS2", "PTDRTKI", getArretsData('B')))
        list.add(Line('C', "DUCS1", "DUCS2", getArretsData('C')))
        list.add(Line('D', "CHALL2", "CHALL1", getArretsData('D')))

        return list
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_lines, container, false)

        recyclerView = root.findViewById(R.id.lines_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = LineAdapter(getLinesData())

        LineAdapter.setOnClickListener(object :
            LineAdapter.OnClickListener {
            override fun onClick(view1: RecyclerView) {
                view1.isVisible = !view1.isVisible
                Log.i("EHOOOO", view1.isVisible.toString())
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}