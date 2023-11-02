package com.dubert.synchrotron

import android.os.Bundle
import android.util.Log
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


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UpdateDatabase()

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMenu.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favs, R.id.nav_lines, R.id.nav_ticket
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun UpdateDatabase() {

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val letters = arrayOf('A', 'B', 'C', 'D');
        val url = "https://start.synchro.grandchambery.fr/fr/map/linesshape?line="

        for (letter in letters){
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url+letter, null,
                Response.Listener { response ->
                    val tab = response.getJSONArray(letter.toString())
                    val arretStorage = ArretJSONFileStorage.getInstance(this)
                    val arretList = ArrayList<String>()

                    for (i in 0 until tab.length()){
                        val lineJson = tab.getJSONObject(i)
                        val isOpposite = if (lineJson.getString("direction") == "RETURN") true else false
                        val arretsJson = lineJson.getJSONArray("stopPoints")

                        for (j in 0 until arretsJson.length()){
                            val arret = arretsJson.getJSONObject(j)
                            val code = arret.getString("id")
                            arretList.add(code)

                            if (arretStorage.findByCode(code) == null){
                                var oppositeCode = ""
                                if (arret.has("oppositeStopPoint")){
                                    oppositeCode = arret.getJSONObject("oppositeStopPoint").getString("id")
                                }else if (code == "HYERE2"){
                                    oppositeCode = "HYERE1"
                                }else if (code == "HYERE1"){
                                    oppositeCode = "HYERE2"
                                }
                                /*
                                * Désolé pour l'atrocité au-dessus, ce cas spécifique se produit car Synchrobus a oublié d'ajouter la donnée.
                                * L'erreur est également présente dans leur application, l'arrêt Hyeres (ligne C) est le seul arrêt
                                * à ne pas pouvoir changer de sens, alors que l'autre sens existe, ainsi, l'arrêt de code HYERE1 n'est pas
                                * accessible depuis l'onglet ligne.
                                * */

                                arretStorage.insert(
                                    Arret(-1,
                                        code,
                                        arret.getString("name"),
                                        arret.getDouble("lat").toFloat(),
                                        arret.getDouble("lon").toFloat(),
                                        arret.has("isArrival"),
                                        arret.has("isTerminus"),
                                        false,
                                        isOpposite,
                                        oppositeCode
                                    ))
                            }
                        }
                    }

                    arretStorage.getLines()[letter] = Line(letter, arretList, Line.charToLineLogo(letter))


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