package com.dubert.synchrotron.model

class Arret(
    var id : Int,
    var code : String,
    var name : String,
    var lat : Float,
    var lon : Float,
    var isArrival : Boolean,
    var isTerminus : Boolean,
    var isFavorite : Boolean,
    var isOpposite : Boolean,
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
        const val ISOPPOSITE = "isOpposite"
        const val OPPOSITE = "opposite"
    }

    fun getNextBus(): String {
        return "5" // TODO : REPLACE WITH REAL NUMBER
    }
}