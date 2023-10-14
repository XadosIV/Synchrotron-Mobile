package com.dubert.synchrotron.ui.favs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Eh ça c'est les favoris"
    }
    val text: LiveData<String> = _text
}