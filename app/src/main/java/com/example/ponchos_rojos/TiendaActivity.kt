package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.databinding.ActivityCartBinding
import com.example.ponchos_rojos.databinding.TiendaActivityBinding
import org.json.JSONArray
import java.io.IOException

class TiendaActivity : AppCompatActivity() {

    private val context: Context = this
    private lateinit var binding: TiendaActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= TiendaActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //setContentView(R.layout.activity_tienda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ESTO ES PARA LA SEARCH BAR QUE SE OCULTE CUANDO ESCRIBAS
        val searchEditText = findViewById<EditText>(R.id.textview1)
        val searchIcon = findViewById<ImageView>(R.id.imageview1)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.isNullOrEmpty()) {
                    searchIcon.isVisible = true
                } else {
                    searchIcon.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        val gamesRecyclerView = findViewById<RecyclerView>(R.id.gamesRecyclerView)
        gamesRecyclerView.layoutManager = LinearLayoutManager(this)

        val gameList = loadGamesFromJson()
        if (gameList.isNotEmpty()) {
            val gameAdapter = GameAdapter(this, gameList)
            gamesRecyclerView.adapter = gameAdapter
        }


        //intent pantalla a carrito
        binding.buttonimageCart.setOnClickListener {

            val intent = Intent(context, activity_cart::class.java)
            // intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)
        }

       binding.buttonimageLibrary.setOnClickListener {
           val intent = Intent(context, activity_library::class.java)
           // intent.putExtra("gameData", game) // enviamos el objeto completo
           context.startActivity(intent)
       }




    }
    private fun loadGamesFromJson(): List<GameInfo> {
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
