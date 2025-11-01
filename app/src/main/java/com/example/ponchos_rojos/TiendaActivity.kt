package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.databinding.ActivityCartBinding
import com.example.ponchos_rojos.databinding.ActivityTiendaBinding
//import com.example.ponchos_rojos.databinding.ActivityTiendaBinding
import org.json.JSONArray

class TiendaActivity : AppCompatActivity() {

    private val context: Context = this
    private lateinit var binding: ActivityTiendaBinding


    //private lateinit var binding: ActivityTiendaBinding
    private lateinit var gameAdapter: GameAdapter
    private var gameList: List<GameInfo> = listOf()
    private val FEATURED_TAG = "Featured"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= ActivityTiendaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //setContentView(R.layout.activity_tienda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // FUNCION PARA QUE SE MUESTRE O OCULTE LA LUPA EN LA BARRA DE BUSQUEDA
        setupSearchIconVisibility()
        // CREACION DE RECYCLER VIEW USANDO EL JSON DE GAMES Y CARGANDO TODOS AL PRINCIPIO
        gameList = loadGamesFromJson()
        gameAdapter = GameAdapter(this, gameList)
        binding.gamesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.gamesRecyclerView.adapter = gameAdapter
        // FUNCION PARA FILTRAR LOS JUEGOS POR TAG
        setupTagSpinner()
        // FUNCION PARA BUSCAR CON NOMBRE DE JUEGO TOMANDO EN CUENTA LOS TAGS
        setupSearchByText()
    }

    private fun setupSearchIconVisibility() {
        binding.edittext1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.isNullOrEmpty()) {
                    binding.imageview1.isVisible = true
                } else {
                    binding.imageview1.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setupSearchByText() {
        binding.edittext1.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val searchQuery = v.text.toString()

                val filteredList = if (searchQuery.isBlank()) {
                    gameList
                } else {
                    gameList.filter { game ->
                        game.name.startsWith(searchQuery, ignoreCase = true)
                    }
                }
                gameAdapter.updateList(filteredList)

                if (searchQuery.isNotEmpty()) {
                    binding.FeaturedNestedText.text = "RESULTS FOR: ${searchQuery.uppercase()}"
                } else {
                    binding.FeaturedNestedText.text = FEATURED_TAG.uppercase()
                }

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setupTagSpinner() {
        val allTags = gameList
            .flatMap { it.tags }
            .distinct()
            .sorted()

        val spinnerOptions = mutableListOf(FEATURED_TAG)
        spinnerOptions.addAll(allTags)

        val spinnerAdapter = TagSpinnerAdapter(this, spinnerOptions)

        binding.tagSpinner.adapter = spinnerAdapter

        binding.tagSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val selectedTag = parent?.getItemAtPosition(position).toString()

                filterGamesByTag(selectedTag)

                if (selectedTag == FEATURED_TAG) {
                    binding.FeaturedNestedText.text = "FEATURED"
                    return
                } else {
                    binding.FeaturedNestedText.text = "FILTERED BY: ${selectedTag.uppercase()}"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
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

    private fun filterGamesByTag(tag: String) {
        val filteredList = if (tag == FEATURED_TAG) {
            gameList
        } else {
            gameList.filter { game ->
                game.tags.contains(tag)
            }
        }
        gameAdapter.updateList(filteredList)
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
