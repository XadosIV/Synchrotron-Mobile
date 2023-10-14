package com.dubert.synchrotron.ui.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TicketViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "ACHETEZ UN TICKET"
    }
    val text: LiveData<String> = _text
}