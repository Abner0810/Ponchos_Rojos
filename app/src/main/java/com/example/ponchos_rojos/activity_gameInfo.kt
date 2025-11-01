package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ponchos_rojos.adapters.AdapterRecyclerCart
import com.example.ponchos_rojos.adapters.AdapterRecyclerTagsButton
import com.example.ponchos_rojos.databinding.ActivityGameInfoBinding
import com.example.ponchos_rojos.databinding.ActivityLoginBinding
import kotlinx.serialization.json.Json
import org.json.JSONArray

class activity_gameInfo : AppCompatActivity() {

    private lateinit var binding: ActivityGameInfoBinding
    private val context: Context = this
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameInfoBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        ////TIENES QUE TENER ESTO SIOSI EN CUALQUIER CLASE/ADAPTADOR EN EL QUE QUIERAS LLAMAR A SHARED PREFERENCES
        ////SI NO LA APP CRASHEA
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        //setContentView(R.layout.activity_game_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val mediaController = MediaController(this)
//        mediaController.setAnchorView(binding.eldenringVideo)


        val hideDelay = 3000L

        fun showButtonTemporarily(buttonToShow: ImageButton) {
            buttonToShow.visibility = View.VISIBLE

            buttonToShow.postDelayed({
                buttonToShow.visibility = View.GONE
            }, hideDelay)
        }

//        val uri = Uri.parse("android.resource://" + "com.example.ponchos_rojos" + "/" + R.raw.elden_ring_compressed)
//        binding.gameVideo.setVideoURI(uri)
//        //binding.eldenringVideo.setMediaController(mediaController)
//        // Play button
//                binding.playButton.setOnClickListener {
//                    binding.gameVideo.start()
//                    binding.playButton.visibility = View.GONE      // se oculta inmediatamente
//                    binding.pauseButton.visibility = View.VISIBLE  // aparece pause
//                    showButtonTemporarily(binding.pauseButton)     // desaparece después de hideDelay
//                }


//// Pause button
//        binding.pauseButton.setOnClickListener {
//            binding.gameVideo.pause()
//            binding.pauseButton.visibility = View.GONE     // se oculta inmediatamente
//            binding.playButton.visibility = View.VISIBLE   // aparece play
//            showButtonTemporarily(binding.playButton)      // desaparece después de hideDelay
//        }
//
//        binding.gameVideo.setOnTouchListener { v, _ ->
//            v.performClick()
//            if (binding.gameVideo.isPlaying) {
//                // Si está reproduciendo, mostramos el botón de pausa
//                binding.pauseButton.visibility = View.VISIBLE
//                binding.playButton.visibility = View.GONE
//                showButtonTemporarily(binding.pauseButton)
//            } else {
//                // Si está pausado, mostramos el botón de play
//                binding.playButton.visibility = View.VISIBLE
//                binding.pauseButton.visibility = View.GONE
//                showButtonTemporarily(binding.playButton)
//            }
//            true
//        }

//        binding.addToCartButton.setOnClickListener {
//            var ver = false



        // }


        // recibir informacion de la TIENDAACTIVITY

        val game = intent.getSerializableExtra("gameData") as? GameInfo

        // Verifica que no sea nulo
        if (game != null) {
            binding.gameTitleText.text = game.name
            binding.nameGameToAddCartText.text = game.name
            binding.gameDeveloperInfo.text = game.developer
            binding.gameDateInfo.text = game.releasedDate
            binding.gameDescription.text = game.description
            binding.priceGameText.text = game.price
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
            val adapterRecyclerTags = AdapterRecyclerTagsButton(this, game?.tags ?: emptyList())
            binding.recyclerTagsButton.adapter = adapterRecyclerTags
        }

        //intents
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


        // AGREGADO Y REMOCION DE JUEGOS DEL CARRITO CON SHARED PREFERENCES

        // HABILITAMOS Y GUARDAMOS EL EDITOR EN UNA VARIABLE
        val editor = sharedPreferences.edit()

        //VERIFICACION DEL BOTON DE ADDTOCART PARA QUE SEA PERDURABLE
        // A PESAR DE QUE PARECE QUE LO DECLARAMOS EN FALSE CADA QUE SE LO LLAMA SIMPLEMENTE ES SU VALOR PREDETERMINADO EN CASO DE QUE NUNCA
        // SE HAYA CREADO DENTRO DE SHARED PREFERENCES LA CLAVE:VALOR
        val botonFuePresionado = sharedPreferences.getBoolean("boton_presionado_${game!!.name}", false)


        // ESTE IF DETEMRINA CUAL DE LOS DOS BOTONES APARECERA AL ABRIR LA APLICACION
        if (botonFuePresionado) {
            binding.addToCartButton.visibility = View.GONE      // se oculta inmediatamente
            binding.addedToCartButton.visibility = View.VISIBLE


        } else {
            binding.addedToCartButton.visibility = View.GONE     // se oculta inmediatamente
            binding.addToCartButton.visibility = View.VISIBLE
        }

        binding.addToCartButton.setOnClickListener {
            editor.putBoolean("boton_presionado_${game.name}", true)
            binding.addToCartButton.visibility = View.GONE      // se oculta inmediatamente
            binding.addedToCartButton.visibility = View.VISIBLE  // aparece pause


            // EL SIGNO !! ES PARA DECIRLE QUE ESTAMOS SEGUROS DE QUE LO QUE ESTAMOS RECIBIENDO NO ESTA VACIO
            val idGame = game!!.id.toString()

            // COLOCAR DENTRO DEL CARRITO
            editor.putString("idGame_${game.name}", idGame)
            editor.apply()
        }

        // Pause button
        binding.addedToCartButton.setOnClickListener {
            editor.putBoolean("boton_presionado_${game.name}", false)

            binding.addedToCartButton.visibility = View.GONE     // se oculta inmediatamente
            binding.addToCartButton.visibility = View.VISIBLE   // aparece play

            //LO QUITAMOS DEL CARRITO
            //IGUAL EXISTE OTRA FUNCION PARA QUITAR TOOD DENTRO DE SHARED PREFERENCES
            editor.remove("idGame_${game.name}").apply()

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