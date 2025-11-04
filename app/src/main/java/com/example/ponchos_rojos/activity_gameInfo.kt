package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ponchos_rojos.adapters.AdapterRecyclerTags
import com.example.ponchos_rojos.databinding.ActivityGameInfoBinding
import android.view.inputmethod.InputMethodManager
import com.example.ponchos_rojos.DataClass.GameInfo
import org.json.JSONArray

class activity_gameInfo : AppCompatActivity() {
    private lateinit var binding: ActivityGameInfoBinding
    private val context: Context = this
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesButton: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameInfoBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ////TIENES QUE TENER ESTO SIOSI EN CUALQUIER CLASE/ADAPTADOR EN EL QUE QUIERAS LLAMAR A SHARED PREFERENCES
        ////SI NO LA APP CRASHEA
        sharedPreferences = getSharedPreferences("JuegosCarrito", Context.MODE_PRIVATE)
        sharedPreferencesButton = getSharedPreferences("logicButton", Context.MODE_PRIVATE)


        //setContentView(R.layout.activity_game_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // recibir informacion de la TIENDAACTIVITY

        val game = intent.getSerializableExtra("gameData") as? GameInfo

        // Verifica que no sea nulo
        if (game != null) {
            binding.gameTitleText.text = game.name
            binding.nameGameToAddCartText.text = game.name
            binding.gameDeveloperInfo.text = game.developer
            binding.gameDateInfo.text = game.releasedDate
            binding.gameDescription.text = game.description
            binding.priceGameText.text = "$"+game.price
            binding.soInfoText.text = game.so
            binding.processorInfoText.text = game.processor
            binding.memoryInfoText.text = game.memory
            binding.graphicsInfoText.text = game.graphics
            binding.storageInfoText.text = game.storage

            // Y cualquier otro dato

            val imageId = resources.getIdentifier(game.imageName, "drawable", packageName)
            if (imageId != 0) {
                binding.gamePortrait.setImageResource(imageId)
                binding.gameVideo.setImageResource(imageId)
            }
        } else {
            Toast.makeText(this, "No se pudo cargar la información del juego.", Toast.LENGTH_SHORT)
                .show()

        }

        //reproduccion de video

        binding.playButton.setOnClickListener {
            // val videoUrl = "https://youtu.be/E3Huy2cdih0?si=Qc0TBNH9h0PFDsI8" // URL del video

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(game?.url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.youtube") // Intenta abrir en la app de YouTube


            startActivity(intent)

        }


        //Configuracion para le recycler de los botones tag


        binding.recyclerTagsButton.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val gameList = loadGamesFromJson()
        if (gameList.isNotEmpty()) {
            val listaTags: List<String> = game?.tags ?: emptyList()
            //  val listaTags = listOf("Action", "Adventure", "Fantasy", "Open World")
            val adapterRecyclerTags = AdapterRecyclerTags(this, listaTags)
            binding.recyclerTagsButton.adapter = adapterRecyclerTags
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

        binding.buttonimageLibrary.setOnClickListener {
            val intent = Intent(context, activity_library::class.java)
            // intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)
        }

        // INTENT PERFIL USUARIO
        binding.imageProfile.setOnClickListener {
            startActivity(Intent(context, MainPerfilActivity::class.java))
        }


        // AGREGADO Y REMOCION DE JUEGOS DEL CARRITO CON SHARED PREFERENCES

        // HABILITAMOS Y GUARDAMOS EL EDITOR EN UNA VARIABLE
        val editor = sharedPreferences.edit()
        val editorButton = sharedPreferencesButton.edit()

        //VERIFICACION DEL BOTON DE ADDTOCART PARA QUE SEA PERDURABLE
        // A PESAR DE QUE PARECE QUE LO DECLARAMOS EN FALSE CADA QUE SE LO LLAMA SIMPLEMENTE ES SU VALOR PREDETERMINADO EN CASO DE QUE NUNCA
        // SE HAYA CREADO DENTRO DE SHARED PREFERENCES LA CLAVE:VALOR
        val botonFuePresionado = sharedPreferencesButton.getBoolean("boton_presionado_${game!!.name}", false)


        // ESTE IF DETEMRINA CUAL DE LOS DOS BOTONES APARECERA AL ABRIR LA APLICACION
        if (botonFuePresionado) {
            binding.addToCartButton.visibility = View.GONE      // se oculta inmediatamente
            binding.addedToCartButton.visibility = View.VISIBLE


        } else {
            binding.addedToCartButton.visibility = View.GONE     // se oculta inmediatamente
            binding.addToCartButton.visibility = View.VISIBLE
        }

        binding.addToCartButton.setOnClickListener {
            editorButton.putBoolean("boton_presionado_${game.name}", true)
            binding.addToCartButton.visibility = View.GONE      // se oculta inmediatamente
            binding.addedToCartButton.visibility = View.VISIBLE  // aparece pause


            // EL SIGNO !! ES PARA DECIRLE QUE ESTAMOS SEGUROS DE QUE LO QUE ESTAMOS RECIBIENDO NO ESTA VACIO
            val idGame = game.id.toString()

            // COLOCAR DENTRO DEL CARRITO
            editor.putString("idGame_${game.name}", idGame)
            editor.apply()
            editorButton.apply()
        }

        // Pause button
        binding.addedToCartButton.setOnClickListener {
            editorButton.putBoolean("boton_presionado_${game.name}", false)

            binding.addedToCartButton.visibility = View.GONE     // se oculta inmediatamente
            binding.addToCartButton.visibility = View.VISIBLE   // aparece play

            //LO QUITAMOS DEL CARRITO
            //IGUAL EXISTE OTRA FUNCION PARA QUITAR TOOD DENTRO DE SHARED PREFERENCES
            editor.remove("idGame_${game.name}").apply()
            editorButton.apply()
        }



        ///////JUEGOS COMPRADOS


        val botuttonGamePurchased = sharedPreferencesButton.getBoolean("button_gameBuyed_${game.name}", false)


        // ESTE IF DETEMRINA CUAL DE LOS DOS BOTONES APARECERA AL ABRIR LA APLICACION
        if (botuttonGamePurchased) {
            binding.addToCartButton.visibility = View.GONE      // se oculta inmediatamente
            binding.addedToCartButton.visibility = View.GONE
            binding.onYourLibraryText.visibility = View.VISIBLE


        }

        setupSearchIconVisibility()
        setupSearchRedirect()
    }

    // Ocultar Lupa
    private fun setupSearchIconVisibility() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.isNullOrEmpty()) {
                    binding.searchIcon.isVisible = true
                } else {
                    binding.searchIcon.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    // Manda el texto de search bar

    private fun setupSearchRedirect() {
        binding.searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val searchQuery = v.text.toString()
                if (searchQuery.isNotBlank()) {
                    val intent = Intent(context, TiendaActivity::class.java)

                    intent.putExtra("SEARCH_QUERY", searchQuery)

                    startActivity(intent)

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)

                    return@setOnEditorActionListener true
                }
            }
            return@setOnEditorActionListener false
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