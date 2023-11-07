package com.dubert.synchrotron.model

import android.util.Log

class Arret(
    var id : Int,
    var code : String,
    var name : String,
    var lat : Float,
    var lon : Float,
    var isArrival : Boolean,
    var isTerminus : Boolean,
    var isFavorite : Boolean,
    var opposite : String
) {

    companion object {
        const val ID = "id"
        const val CODE = "code"
        const val NAME = "name"
        const val LAT = "lat"
        const val LON = "lon"
        const val ARRIVAL = "isArrival"
        const val FAVORITE = "isFavorite"
        const val TERMINUS = "isTerminus"
        const val OPPOSITE = "opposite"
    }

    fun urlToNextBus(body:String, line:Char?): ArrayList<NextBus> {
        val list = arrayListOf<NextBus>()
        var elements = body.split("<div class=\"nq-c-Direction-content\">")
        if (elements.size == 1) return list
        elements = elements.drop(1)
        for (element in elements){
            var l = element.split("_")[0].last()
            if (l in arrayOf('A', 'B', 'C', 'D')){
                if (l == line || line == null){ // Recuperer uniquement les bus de la bonne ligne.
                    list.add(
                        NextBus(
                            element.split("detail-time\">")[1].split("<")[0], //horaire
                            element.split("<span>")[1].split("</span>")[0], // direction
                            l, //line
                            code
                        ))
                }
            }

        }
        Log.i("SIZEOF", ""+list.size)
        return list
    }
}