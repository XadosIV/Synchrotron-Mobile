package com.dubert.synchrotron.ui.ticket

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dubert.synchrotron.R
import com.dubert.synchrotron.databinding.FragmentHomeBinding

class TicketFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var buttonBuy : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_ticket, container, false)
        buttonBuy = root.findViewById(R.id.button_id)

        buttonBuy.setOnClickListener { root ->
            confirmBuy()
        }

        return root
    }

    private fun confirmBuy() {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.ticket_confirm))
        builder.setMessage(getString(R.string.ticket_confirm_text) + " " + getString(R.string.ticket_prix))
        builder.setPositiveButton(getString(R.string.oui), { dialog, id ->
            buyTicket()
            dialog.cancel()
        })
        builder.setNegativeButton(getString(R.string.non), { dialog, id ->
            dialog.cancel()
        })
        val alert = builder.create()
        alert.show()
    }

    private fun buyTicket() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}