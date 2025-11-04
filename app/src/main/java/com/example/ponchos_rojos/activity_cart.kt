package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ponchos_rojos.DataClass.GameInfo
import com.example.ponchos_rojos.adapters.AdapterRecyclerCart
import com.example.ponchos_rojos.databinding.ActivityCartBinding
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.util.Locale

class activity_cart : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val context: Context = this
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityCartBinding.inflate(layoutInflater)

        enableEdgeToEdge()

        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("JuegosCarrito", Context.MODE_PRIVATE)

        //setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//////////IMPLEMENTACION DEL RECYCLER PARA LAS TARJETAS DE CARRITO Y DE SHARED PREFERENCES PARA LA PERDURACION DE DATOS
        binding.payButton.visibility = View.GONE
        binding.priceText.text = "$0.0"

        binding.recyclerCartGames.layoutManager = LinearLayoutManager(this)

        val gameEntireList = loadGamesFromJson()



        ////CREAMOS OTRA LISTA PARA PONER LOS JUEGOS QUE SE INGRESARON EN SHARED PREFERENCES DESDE LA PANTALLA DE INFO GAMES CON EL BOTON ADDTOCART
        val selectedListGames: MutableList<GameInfo> = mutableListOf()
        ////LLENAMOS LA NUEVA LISTA DE SELECTEDLISTGAMES COMPARANDO LAS CLAVES DE SHARED PREFERENCES CON LAS DE NUESTRA LISTA TOTAL DE JUEGOS
        for(i in 0 until gameEntireList.size){
            if(sharedPreferences.contains("idGame_${gameEntireList[i].name}")){
                selectedListGames.add(gameEntireList[i])
            }
        }

        //LOGICA DE PRECIOS E IMPLEMENTACION DEL ADAPTER
        if (selectedListGames.isNotEmpty()) {
            val AdapterRecyclerCart = AdapterRecyclerCart(this, selectedListGames,binding.yourcartIsemptyTitle,binding.priceText,binding.payButton)
            binding.recyclerCartGames.adapter = AdapterRecyclerCart

            var suma:Double = 0.0
            var decimal:String =""
            if(!selectedListGames.isEmpty()){
                // suma de precio total
                for(i in 0 until  selectedListGames.size){
                    suma += selectedListGames[i].price.toDouble()
                }
                decimal = String.format(Locale.US,"%.2f", suma)

                //si la lista no esta vacia puedes comprar

                    binding.payButton.visibility = View.VISIBLE



            }else{
                //si no hay nada en la lista no puedes comprar nada
                binding.payButton.visibility = View.GONE
            }

            val totalString: String = decimal
            binding.priceText.text = "$$totalString"

            ///

            binding.yourcartIsemptyTitle.visibility = View.GONE

        }else{
            binding.yourcartIsemptyTitle.visibility = View.VISIBLE

        }




        //INTENTS ENTRE PANTALLAS PRINCIPALES

        binding.buttonimageTag.setOnClickListener {

            val intent = Intent(context, TiendaActivity::class.java)
            // intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)
        }

        binding.buttonimageLibrary.setOnClickListener {
            val intent = Intent(context, activity_library::class.java)
            // intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)
        }

        binding.payButton.setOnClickListener {
            val intent = Intent(context, PagarActivity::class.java)
            // intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)
        }


        // INTENT PERFIL USUARIO
        binding.imageProfile.setOnClickListener {
            startActivity(Intent(context, MainPerfilActivity::class.java))
        }
    }



    private fun guardarDataClass(proyecto: GameInfo) {
        val asdfgh: String = Json.encodeToString(proyecto)
        val editor = sharedPreferences.edit()
        editor.putString("datosProyecto", asdfgh)
        editor.apply()
    }

    private fun obtenerDataClass(): GameInfo? {
        val datoGuardado: String = sharedPreferences.getString("datosProyecto", null) ?: ""
        // binding.textViewDatosSharedPrefs.text = datoGuardado
        if (!datoGuardado.isEmpty()) {
            val objetoGuardado = Json.decodeFromString<GameInfo>(datoGuardado)
            return objetoGuardado
        }
        return null
    }

    private fun obtenerDataDeFile(): GameInfo? {
        val fileString: String =
            applicationContext.assets.open("games.json").bufferedReader().use { it.readText() }
        // binding.textViewDatosLocalFile.text = fileString
        val objetoGuardado = Json.decodeFromString<GameInfo>(fileString)
        return objetoGuardado
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
