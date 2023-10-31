package com.dubert.synchrotron.ui.lines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.LineAdapter
import com.dubert.synchrotron.R
import com.dubert.synchrotron.databinding.FragmentHomeBinding
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class LinesFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var recyclerView : RecyclerView

    private fun getLinesData(): ArrayList<Line> {
        val arretStorage = context?.let { ArretJSONFileStorage.getInstance(it) }
        val lines = arretStorage!!.getLines()
        val list = arrayListOf<Line>()
        val letters = arrayOf('A', 'B', 'C', 'D');
        for (letter in letters) {
            list.add(lines.getValue(letter))
        }
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

        recyclerView.adapter = LineAdapter(getLinesData(), recyclerView)
        LineAdapter.setOnClickListener(object :
            LineAdapter.OnClickListener {
            override fun onClick(view1: RecyclerView, image: ImageView) {
                view1.isVisible = !view1.isVisible
                if (view1.isVisible){
                    image.setImageResource(R.drawable.ic_arrow_up)
                } else {
                    image.setImageResource(R.drawable.ic_arrow_down)
                }

            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}