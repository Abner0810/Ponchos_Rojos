package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.databinding.ActivityPagarBinding
import org.json.JSONArray

class PagarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagarBinding
    private val context: Context = this
    private lateinit var sharedPreferencesCarrito: SharedPreferences
    private lateinit var sharedPreferencesLibrary: SharedPreferences
    private lateinit var sharedPreferencesButton: SharedPreferences




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPagarBinding.inflate(layoutInflater)
        sharedPreferencesCarrito = getSharedPreferences("JuegosCarrito", Context.MODE_PRIVATE)
        sharedPreferencesLibrary = getSharedPreferences("JuegosLibrary", Context.MODE_PRIVATE)
        sharedPreferencesButton = getSharedPreferences("logicButton", Context.MODE_PRIVATE)


        setContentView(binding.root)
        val pagos = resources.getStringArray(R.array.pagos)
        val meses = resources.getStringArray(R.array.meses)
        val anios = resources.getStringArray(R.array.anios)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pagos)
        val adapter_mes = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, meses
        )
        val adapter_anio = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, anios
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter_mes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter_anio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.menuPago.adapter = adapter
        binding.fechaMes.adapter = adapter_mes
        binding.fechaAnio.adapter = adapter_anio
        //escogerpago
        binding.menuPago.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.fechaMes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.fechaAnio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.flecha.setOnClickListener {
            finish()
        }




        binding.btnPagar.setOnClickListener {

            val editorLibrary = sharedPreferencesLibrary.edit()
            val editorCarrito = sharedPreferencesCarrito.edit()
            val editorButton = sharedPreferencesButton.edit()

            var isValid = true

            if (!areAllFieldsFilled()) {
                isValid = false
            }

            if (!telephoneVerification()) {
                binding.rellenarCelular.error = "invalid phone number"
                isValid = false
            }

            if (!cardVerification()) {
                binding.rellenarTarjeta.error = "invalid card number"

                isValid = false
            }

            if (!securityCodeVerification()) {
                binding.codigoSegRellenar.error = "invalid security code"

                isValid = false
            }

            if (!postalCodeVerification()) {
                binding.rellenarCodigoPostal.error = "invalid postal code"


                isValid = false
            }


                // si alguna validación falló, detenemos la ejecución
            if (!isValid) {
                Toast.makeText(context, "Please fill all the fields correctly", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }







            // AÑADIR JUEGOS A LA LIBRERIA

            val gameEntireList = loadGamesFromJson()



            ////CREAMOS OTRA LISTA PARA PONER LOS JUEGOS QUE SE INGRESARON EN SHARED PREFERENCES DESDE LA PANTALLA DE INFO GAMES CON EL BOTON ADDTOCART
            //val selectedListGames: MutableList<GameInfo> = mutableListOf()
            ////LLENAMOS LA NUEVA LISTA DE SELECTEDLISTGAMES COMPARANDO LAS CLAVES DE SHARED PREFERENCES CON LAS DE NUESTRA LISTA TOTAL DE JUEGOS
            for(i in 0 until gameEntireList.size){
                if(sharedPreferencesCarrito.contains("idGame_${gameEntireList[i].name}")){

                    editorLibrary.putInt("idGames_${gameEntireList[i].name}",gameEntireList[i].id).apply()
                    editorButton.putBoolean("button_gameBuyed_${gameEntireList[i].name}",true ).apply()

                }
            }


            editorCarrito.clear()
            editorCarrito.apply()

            val intent = Intent(context, TiendaActivity::class.java)
            // intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)



           // val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
           // val editor = prefs.edit()
           // val keysToRemove = prefs.all.keys.filter { it.startsWith("idGame_") }
           // for (k in keysToRemove) editor.remove(k)
           // editor.apply()
           // Toast.makeText(
           //     this@PagarActivity, "Pago realizado correctamente", Toast.LENGTH_SHORT
           // ).show()
           // finish()

        }
        binding.avatarBtn.setOnClickListener {
            startActivity(Intent(context, MainPerfilActivity::class.java))
        }







    }

    private fun telephoneVerification():Boolean{
       var numPhone = binding.rellenarCelular.text.toString()

        if(numPhone.length < 8){

            return false

        }
        return true

    }
    private fun cardVerification():Boolean{
        var cardNumber = binding.rellenarTarjeta.text.toString()

        if(cardNumber.length != 16){

            return false

        }
        return true

    }

    private fun securityCodeVerification():Boolean{
        var securityNumber = binding.codigoSegRellenar.text.toString()

        if(securityNumber.length != 3){

            return false

        }
        return true

    }
    private fun postalCodeVerification():Boolean{
        var postalNumber = binding.rellenarCodigoPostal.text.toString()

        if(postalNumber.length < 5){

            return false

        }
        return true

    }





    private fun areAllFieldsFilled(): Boolean {
        val fields = listOf<EditText>(
            binding.rellenarTarjeta,
            binding.codigoSegRellenar,
            binding.rellenarNombre,
            binding.rellenarApellido,
            binding.rellenarDireccion,
            binding.rellenarCiudad,
            binding.rellenarCodigoPostal,
            binding.rellenarPais,
            binding.rellenarCelular
        )

        for (field in fields) {
            val text = field.text.toString().trim()
            if (text.isEmpty()) {
                field.requestFocus()
                Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
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
