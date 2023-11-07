package com.dubert.synchrotron.model

import android.util.Log
import com.dubert.synchrotron.R
import com.dubert.synchrotron.storage.ArretJSONFileStorage

class Line(
    var name : Char,
    var forward : ArrayList<String>,
    var backward : ArrayList<String>,
) {
    companion object {
        fun charToLineLogo(line : Char): Int {
            return when (line.uppercaseChar()){
                'A' -> R.drawable.line_a
                'B' -> R.drawable.line_b
                'C' -> R.drawable.line_c
                'D' -> R.drawable.line_d
                else -> R.drawable.ic_star //debug case
            }
        }
    }

    fun getTerminus() : ArrayList<Arret> {
        val list = ArrayList<Arret>()
        val storage = ArretJSONFileStorage.getInstance()
        for (code in forward + backward){
            val arret = storage.findByCode(code)
            if (arret?.isTerminus == true && arret !in list){
                list.add(arret)
            }
        }
        if (list.size != 2){
            Log.i("ERROR", list[0].name)
        }
        return list
    }

    fun logo() : Int{
        return charToLineLogo(name)
    }
}