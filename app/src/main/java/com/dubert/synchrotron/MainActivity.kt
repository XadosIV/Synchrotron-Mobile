package com.dubert.synchrotron

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(R.layout.activity_charge) {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myIntent = Intent(this, ApplicationActivity::class.java)
        GlobalScope.launch{
            suspend {
                updateDatabase()
                Log.i("STARTED", "JAI STARTED")
                startActivity(myIntent)
            }.invoke()
        }
    }

    private suspend fun updateDatabase() {
        Log.i("STARTED", "JAI RECU LE CALL")

        // Create Storage Instance
        ArretJSONFileStorage.init(this)

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val letters = arrayOf('A', 'B', 'C', 'D');
        val url = "https://start.synchro.grandchambery.fr/fr/map/linesshape?line="
        var responseLength = 0
        for (letter in letters){
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url+letter, null,
                { response ->
                    val tab = response.getJSONArray(letter.toString())
                    val arretStorage = ArretJSONFileStorage.getInstance()
                    val forwardList = ArrayList<String>()
                    val backwardList = ArrayList<String>()

                    for (i in 0 until tab.length()){
                        val lineJson = tab.getJSONObject(i)
                        val isOpposite = lineJson.getString("direction") == "RETURN"
                        val arretsJson = lineJson.getJSONArray("stopPoints")

                        for (j in 0 until arretsJson.length()){
                            val arret = arretsJson.getJSONObject(j)
                            val code = arret.getString("id")
                            if (isOpposite){
                                backwardList.add(code)
                            }else{
                                forwardList.add(code)
                            }

                            if (arretStorage.findByCode(code) == null){
                                var oppositeCode = ""
                                if (arret.has("oppositeStopPoint")) {
                                    oppositeCode = arret.getJSONObject("oppositeStopPoint").getString("id")
                                }

                                // ============= CAS SPECIFIQUE =============
                                if (code == "PRSON" && !arret.has("isTerminus")) continue
                                /*
                                Désolé pour ça, il se trouve que PRSON apparait deux fois dans ces données
                                (C'est le seul arrêt à ne pas faire de distinction avec des chiffres (PRSON1 & PRSON2 n'existent pas)
                                Ainsi, on garde uniquement l'arrêt PRSON ayant des données COMPLETES (l'autre n'ayant pas de valeur pour isTerminus)
                                 */
                                // ===========================================================================

                                arretStorage.insert(
                                    Arret(-1,
                                        code,
                                        arret.getString("name"),
                                        arret.getDouble("lat").toFloat(),
                                        arret.getDouble("lon").toFloat(),
                                        arret.has("isArrival"),
                                        arret.has("isTerminus"),
                                        false,
                                        oppositeCode
                                    ))
                            }
                        }
                    }

                    arretStorage.getLines()[letter] = Line(letter, forwardList, backwardList)
                    responseLength++

                },
                { error ->
                    Toast.makeText(this, "Website didn't respond", Toast.LENGTH_LONG).show();
                    Log.i("LINE_RESPONSE", "FAILED")
                    responseLength++
                }
            )

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        }
        while (responseLength < 4){
            Log.i("STARTED", ""+responseLength)
            delay(50)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}