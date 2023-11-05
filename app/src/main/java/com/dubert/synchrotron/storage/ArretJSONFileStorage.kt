package com.dubert.synchrotron.storage

import android.content.Context
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line
import org.json.JSONObject

class ArretJSONFileStorage (context: Context): JSONFileStorage<Arret>(context, "arrets") {

    companion object {
        private var instance: ArretJSONFileStorage? = null

        private var lines: HashMap<Char, Line>? = null

        fun getInstance(): ArretJSONFileStorage {
            return instance!!
        }

        fun init(context: Context): ArretJSONFileStorage {
            instance = ArretJSONFileStorage(context)
            return instance!!
        }
    }

    override fun create(id: Int, obj:Arret): Arret {
        return Arret(id, obj.code, obj.name, obj.lat, obj.lon, obj.isArrival, obj.isTerminus, obj.isFavorite, obj.opposite)
    }

    override fun objectToJson(id: Int, obj: Arret): JSONObject {
        val json = JSONObject()
        json.put(Arret.ID, obj.id)
        json.put(Arret.NAME, obj.name)
        json.put(Arret.CODE, obj.code)
        json.put(Arret.ARRIVAL, obj.isArrival)
        json.put(Arret.TERMINUS, obj.isTerminus)
        json.put(Arret.FAVORITE, obj.isFavorite)
        json.put(Arret.LAT, obj.lat)
        json.put(Arret.LON, obj.lon)
        json.put(Arret.OPPOSITE, obj.opposite)
        return json
    }

    override fun jsonToObject(json: JSONObject): Arret {
        return Arret(
            json.getInt(Arret.ID),
            json.getString(Arret.CODE),
            json.getString(Arret.NAME),
            json.getDouble(Arret.LAT).toFloat(),
            json.getDouble(Arret.LON).toFloat(),
            json.getBoolean(Arret.ARRIVAL),
            json.getBoolean(Arret.TERMINUS),
            json.getBoolean(Arret.FAVORITE),
            json.getString(Arret.OPPOSITE)
        )
    }

    fun findByCode(code: String): Arret? {
        val t = findAll()
        for (i in t.indices){
            val arret = t[i]
            if (code == arret.code){
                return find(arret.id)
            }
        }
        return null
    }

    fun getLines(): HashMap<Char, Line> {
        if (lines == null){
            lines = HashMap<Char, Line>()
        }
        return lines!!
    }

    fun getLine(code: Char): Line? {
        if (lines == null){
            return null
        }
        return lines!![code]
    }

}