package com.dubert.synchrotron

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dubert.synchrotron.databinding.ActivityMenuBinding
import com.dubert.synchrotron.model.Arret
import com.dubert.synchrotron.model.Line
import com.dubert.synchrotron.storage.ArretJSONFileStorage


class MainActivity : AppCompatActivity(R.layout.activity_charge) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var charging = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UpdateDatabase()
        val img_rotate = findViewById<ImageView>(R.id.loading_circle);
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setFillAfter(true);
        img_rotate.startAnimation(rotation);

        if (!charging) {
            val myIntent = Intent(this, ApplicationActivity::class.java)
            startActivity(myIntent)
        }
    }

    private fun UpdateDatabase() {

        // Create Storage Instance
        ArretJSONFileStorage.init(this)

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val letters = arrayOf('A', 'B', 'C', 'D');
        val url = "https://start.synchro.grandchambery.fr/fr/map/linesshape?line="

        for (letter in letters){
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url+letter, null,
                Response.Listener { response ->
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

                                // ============= CAS SPECIFIQUES =============
                                if (code == "PRSON" && !arret.has("isTerminus")) continue
                                /*
                                Désolé pour ça, il se trouve que PRSON apparait deux fois dans nos données
                                (C'est le seul arrêt à ne pas faire de distinction avec des chiffres (PRSON1 & PRSON2 n'existent pas)
                                Ainsi, on garde uniquement l'arrêt PRSON ayant des données COMPLETES (l'autre n'ayant pas de valeur pour isTerminus)
                                 */

                                if (arret.has("oppositeStopPoint")){
                                    oppositeCode = arret.getJSONObject("oppositeStopPoint").getString("id")
                                }else if (code == "HYERE2"){
                                    oppositeCode = "HYERE1"
                                }else if (code == "HYERE1"){
                                    oppositeCode = "HYERE2"
                                }
                                /*
                                * Encore désolé, mais ce cas spécifique se produit car Synchrobus a oublié d'ajouter la donnée.
                                * L'erreur est également présente dans leur application, l'arrêt Hyeres (ligne C) est le seul arrêt
                                * à ne pas pouvoir changer de sens, alors que l'autre sens existe, ainsi, l'arrêt de code HYERE1 n'est pas
                                * accessible depuis l'onglet ligne.
                                * */
                                // ===========================================================================

                                if (isOpposite){
                                    Log.i("AJOUT-OPPOSITE", code)
                                }else{
                                    Log.i("AJOUT", code)
                                }

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


                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Website didn't respond", Toast.LENGTH_LONG).show();
                }
            )
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}