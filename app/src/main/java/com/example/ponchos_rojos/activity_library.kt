package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ponchos_rojos.adapters.AdapterRecyclerCart
import com.example.ponchos_rojos.adapters.AdapterRecyclerLibrary
import com.example.ponchos_rojos.databinding.ActivityGameInfoBinding
import com.example.ponchos_rojos.databinding.ActivityLibraryBinding
import org.json.JSONArray

class activity_library : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private val context: Context = this
    private lateinit var sharedPreferencesLibrary: SharedPreferences
    private lateinit var sharedPreferencesButton: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLibraryBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        sharedPreferencesLibrary = getSharedPreferences("JuegosLibrary", Context.MODE_PRIVATE)
        sharedPreferencesButton = getSharedPreferences("logicButton", Context.MODE_PRIVATE)


        //setContentView(R.layout.activity_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val images = listOf(
//            R.drawable.beyond_two_souls_image,
//            R.drawable.heavy_rain_image,
//            R.drawable.detroit_become_human_image,
//            R.drawable.hollow_knigth_image,
//            R.drawable.neva_game,
//            R.drawable.celeste_image,
//            R.drawable.aplague_tale_requiem_image,
//            R.drawable.cup_head_image,
//            R.drawable.control_image,
//            R.drawable.cyberpunk_image,
//            R.drawable.eldenring,
//            R.drawable.witcher_image
//
//
//
//
//
//
//            )

//        val listImages = loadGamesFromJson()
//        val adapter = AdapterRecyclerLibrary(listImages)
//        binding.recyclerGames.layoutManager = LinearLayoutManager(this) // o LinearLayoutManager
//        binding.recyclerGames.adapter = adapter

        val gameEntireList = loadGamesFromJson()



        ////CREAMOS OTRA LISTA PARA PONER LOS JUEGOS QUE SE INGRESARON EN SHARED PREFERENCES DESDE LA PANTALLA DE INFO GAMES CON EL BOTON ADDTOCART
        val selectedListGames: MutableList<GameInfo> = mutableListOf()
        ////LLENAMOS LA NUEVA LISTA DE SELECTEDLISTGAMES COMPARANDO LAS CLAVES DE SHARED PREFERENCES CON LAS DE NUESTRA LISTA TOTAL DE JUEGOS
        for(i in 0 until gameEntireList.size){
            if(sharedPreferencesLibrary.contains("idGames_${gameEntireList[i].name}")){
                selectedListGames.add(gameEntireList[i])
            }
        }

        //LOGICA DE PRECIOS E IMPLEMENTACION DEL ADAPTER
        if (selectedListGames.isNotEmpty()) {
            val adapterRecyclerLibrary = AdapterRecyclerLibrary(this,selectedListGames)
            binding.recyclerGames.layoutManager = LinearLayoutManager(this)

            binding.recyclerGames.adapter = adapterRecyclerLibrary
        }






        //intents

        binding.buttonimageTag.setOnClickListener {

            val intent = Intent(context, TiendaActivity::class.java)
            // intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)
        }

        binding.buttonimageCart.setOnClickListener {
            val intent = Intent(context, activity_cart::class.java)
            // intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)
        }

        // INTENT PERFIL USUARIO
        binding.imageProfile.setOnClickListener {
            startActivity(Intent(context, MainPerfilActivity::class.java))
        }
    }





    private fun loadGamesFromJson(): MutableList<GameInfo> {
        val gameList = mutableListOf<GameInfo>()
        val jsonString: String
        jsonString = assets.open("games.json").bufferedReader().use { it.readText() }

        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            val tagsJsonArray = jsonObject.getJSONArray("tags")
            val tagsList = mutableListOf<String>()
            for (j in 0 until tagsJsonArray.length()) {
                tagsList.add(tagsJsonArray.getString(j))
            }

            val game = GameInfo(
                id = jsonObject.getInt("id"),
                name = jsonObject.getString("name"),
                developer = jsonObject.getString("developer"),
                releasedDate = jsonObject.getString("releasedDate"),
                description = jsonObject.getString("description"),
                url = jsonObject.getString("url"),
                tags = tagsList,
                imageName = jsonObject.getString("imageName"),
                price = jsonObject.getString("price"),
                so = jsonObject.getString("so"),
                processor = jsonObject.getString("processor"),
                memory = jsonObject.getString("memory"),
                graphics = jsonObject.getString("graphics"),
                storage = jsonObject.getString("storage")

            )
            gameList.add(game)
        }
        return gameList
    }


}