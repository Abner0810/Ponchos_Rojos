package com.example.ponchos_rojos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.DataClass.UserProfile
import com.example.ponchos_rojos.databinding.ActivityMainPerfilBinding
import com.google.firebase.auth.FirebaseAuth

class MainPerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPerfilBinding
    private lateinit var storage: ProfileStorage
    private lateinit var sharedPreferencesCart: SharedPreferences
    private lateinit var sharedPreferencesLibrary: SharedPreferences
    private lateinit var sharedPreferencesButton: SharedPreferences



    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.let {
                    // ahora también leemos username
                    val username = it.getStringExtra("username")
                    val nombre = it.getStringExtra("nombre")
                    val email = it.getStringExtra("email")
                    val contraseña = it.getStringExtra("contraseña")
                    val celular = it.getStringExtra("celular")
                    val pais = it.getStringExtra("pais")

                    if (!username.isNullOrEmpty()) binding.idUsuario.text = username
                    if (!nombre.isNullOrEmpty()) binding.nombre1.text = nombre
                    if (!email.isNullOrEmpty()) {
                        binding.email1.text = email
                        binding.gmail.text = email
                    }
                    if (!contraseña.isNullOrEmpty()) binding.password1.text = contraseña
                    if (!celular.isNullOrEmpty()) binding.celular1.text = celular
                    if (!pais.isNullOrEmpty()) binding.pais1.text = pais

                    // Guardamos el perfil con username en ProfileStorage
                    val perfil = UserProfile(
                        username = binding.idUsuario.text.toString(),
                        nombre = binding.nombre1.text.toString(),
                        email = binding.email1.text.toString(),
                        contraseña = binding.password1.text.toString(),
                        celular = binding.celular1.text.toString(),
                        pais = binding.pais1.text.toString()
                    )
                    storage.save(perfil)
                    Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = ProfileStorage(this)

        sharedPreferencesCart = getSharedPreferences("JuegosCarrito", Context.MODE_PRIVATE)
        sharedPreferencesLibrary = getSharedPreferences("JuegosLibrary", Context.MODE_PRIVATE)
        sharedPreferencesButton = getSharedPreferences("logicButton", Context.MODE_PRIVATE)


        // Cargar perfil desde storage (incluye username ahora)
        val perfil = storage.load()
        binding.idUsuario.text = perfil.username         // <-- mostrar username junto al avatar
        binding.nombre1.text = perfil.nombre
        binding.email1.text = perfil.email
        binding.gmail.text = perfil.email
        binding.password1.text = perfil.contraseña
        binding.celular1.text = perfil.celular
        binding.pais1.text = perfil.pais

        binding.flecha.setOnClickListener { finish() }

        binding.botoncambiar.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java).apply {
                // ahora enviamos username también
                putExtra("username", binding.idUsuario.text.toString())
                putExtra("nombre", binding.nombre1.text.toString())
                putExtra("email", binding.email1.text.toString())
                putExtra("contraseña", binding.password1.text.toString())
                putExtra("celular", binding.celular1.text.toString())
                putExtra("pais", binding.pais1.text.toString())
            }
            editLauncher.launch(intent)
        }

        binding.botoncerrar.setOnClickListener {
            val editorLibrary = sharedPreferencesLibrary.edit()
            val editorCart = sharedPreferencesCart.edit()
            val editorButton = sharedPreferencesButton.edit()
            editorLibrary.clear().apply()
            editorCart.clear().apply()
            editorButton.clear().apply()


            FirebaseAuth.getInstance().signOut()
            storage.clear() // limpia el perfil local
            val intent = Intent(this, activity_login::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)

        }
    }
}
