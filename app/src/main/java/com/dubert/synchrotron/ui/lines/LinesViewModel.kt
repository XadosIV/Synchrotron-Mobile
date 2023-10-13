package com.dubert.synchrotron.ui.lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LinesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Les lignes et arrêts ici"
    }
    val text: LiveData<String> = _text
}