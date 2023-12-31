package com.dubert.synchrotron.ui.lines

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dubert.synchrotron.adapters.LineAdapter
import com.dubert.synchrotron.R
import com.dubert.synchrotron.databinding.FragmentHomeBinding
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LinesFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var recyclerView : RecyclerView
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private fun getLinesData(): ArrayList<Line> {
        val arretStorage = ArretJSONFileStorage.getInstance()
        val lines = arretStorage!!.getLines()
        val list = arrayListOf<Line>()
        val letters = arrayOf('A', 'B', 'C', 'D');
        for (letter in letters) {
            list.add(lines.getValue(letter))
        }
        return list
    }

    private fun checkPermission(permission: String, view: View): Boolean {
        val context = view.context
        var res = true

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), permission)){
                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(permission), 0)
            }
            res = false
        }
        return res
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_lines, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, root)) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        recyclerView = root.findViewById(R.id.lines_recycler_view)
                        recyclerView.layoutManager = LinearLayoutManager(context)

                        recyclerView.adapter = LineAdapter(getLinesData(), recyclerView, loc)
                    }
                }
        } else {
            recyclerView = root.findViewById(R.id.lines_recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(context)

            recyclerView.adapter = LineAdapter(getLinesData(), recyclerView, null)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}