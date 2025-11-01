package com.example.ponchos_rojos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.databinding.ActivityMainPerfilBinding

class MainPerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPerfilBinding
    private lateinit var storage: ProfileStorage

    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.let {
                    val nombre = it.getStringExtra("nombre")
                    val email = it.getStringExtra("email")
                    val contraseña = it.getStringExtra("contraseña")
                    val celular = it.getStringExtra("celular")
                    val pais = it.getStringExtra("pais")

                    if (!nombre.isNullOrEmpty()) binding.nombre1.setText(nombre)
                    if (!email.isNullOrEmpty()) binding.email1.setText(email)
                    if (!contraseña.isNullOrEmpty()) binding.password1.setText(contraseña)
                    if (!celular.isNullOrEmpty()) binding.celular1.setText(celular)
                    if (!pais.isNullOrEmpty()) binding.pais1.setText(pais)

                    val perfil = UserProfile(
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

        val perfil = storage.load()
        binding.nombre1.setText(perfil.nombre)
        binding.email1.setText(perfil.email)
        binding.password1.setText(perfil.contraseña)
        binding.celular1.setText(perfil.celular)
        binding.pais1.setText(perfil.pais)

        binding.flecha.setOnClickListener { finish() }

        binding.botoncambiar.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java).apply {
                putExtra("nombre", binding.nombre1.text.toString())
                putExtra("email", binding.email1.text.toString())
                putExtra("contraseña", binding.password1.text.toString())
                putExtra("celular", binding.celular1.text.toString())
                putExtra("pais", binding.pais1.text.toString())
            }
            editLauncher.launch(intent)
        }

        binding.botoncerrar.setOnClickListener {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
            storage.clear()
            val intent = Intent(this, activity_login::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }


}

