package com.dubert.synchrotron.ui.ticket

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
            confirmBuy(root)
        }

        return root
    }

    private fun confirmBuy(root:View) {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.ticket_confirm))
        builder.setMessage(getString(R.string.ticket_confirm_text) + " " + getString(R.string.ticket_prix))
        builder.setPositiveButton(getString(R.string.oui), { dialog, id ->
            buyTicket(root)
            dialog.cancel()
        })
        builder.setNegativeButton(getString(R.string.non), { dialog, id ->
            dialog.cancel()
        })
        val alert = builder.create()
        alert.show()
    }

    private fun buyTicket(root:View) {
        if (checkPermission(android.Manifest.permission.SEND_SMS, root)){
            val sentPI: PendingIntent = PendingIntent.getBroadcast(root.context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE)
            SmsManager.getDefault().sendTextMessage("+33629663593", null,"1h", sentPI, null)
            Toast.makeText(root.context, "SMS Envoyé !", Toast.LENGTH_LONG).show()
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}