package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.adapters.AdapterRecyclerCart
import com.example.ponchos_rojos.databinding.ActivityCartBinding
import com.example.ponchos_rojos.databinding.ActivityLibraryBinding
import org.json.JSONArray

class activity_cart : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityCartBinding.inflate(layoutInflater)

        enableEdgeToEdge()

        setContentView(binding.root)

        //setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.recyclerCartGames.layoutManager = LinearLayoutManager(this)

        val gameList = loadGamesFromJson()
        if (gameList.isNotEmpty()) {
            val AdapterRecyclerCart = AdapterRecyclerCart(this, gameList,binding.yourcartIsemptyTitle,binding.priceText,binding.payButton)
            binding.recyclerCartGames.adapter = AdapterRecyclerCart

            var suma:Double = 0.0
            if(!gameList.isEmpty()){
                // suma de precio total
                for(i in 0 until  gameList.size){
                    suma += gameList[i].price.toDouble()
                }

                //si la lista no esta vacia puedes comprar
                binding.payButton.visibility = View.VISIBLE

            }else{
                //si no hay nada en la lista no puedes comprar nada
                binding.payButton.visibility = View.GONE
            }

            val totalString: String = suma.toString()
            binding.priceText.text = "$$totalString"
        }




        //intents

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