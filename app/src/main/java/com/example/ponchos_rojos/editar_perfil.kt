package com.example.ponchos_rojos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.databinding.ActivityEditarPerfilBinding

class EditarPerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPerfilBinding
    private lateinit var storage: ProfileStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = ProfileStorage(this)

        val stored = storage.load()
        binding.Nombre1.setText(intent.getStringExtra("nombre") ?: stored.nombre)
        binding.Email1.setText(intent.getStringExtra("email") ?: stored.email)
        binding.Password1.setText(intent.getStringExtra("contraseña") ?: stored.contraseña)
        binding.Celular1.setText(intent.getStringExtra("celular") ?: stored.celular)
        binding.Pais1.setText(intent.getStringExtra("pais") ?: stored.pais)

        binding.flecha.setOnClickListener { finish() }

        binding.btnGuardar.setOnClickListener {
            if (!areAllFieldsFilled()) {
                return@setOnClickListener
            }

            val perfil = UserProfile(
                nombre = binding.Nombre1.text.toString().trim(),
                email = binding.Email1.text.toString().trim(),
                contraseña = binding.Password1.text.toString(),
                celular = binding.Celular1.text.toString().trim(),
                pais = binding.Pais1.text.toString().trim()
            )

            storage.save(perfil)

            val resultIntent = Intent().apply {
                putExtra("nombre", perfil.nombre)
                putExtra("email", perfil.email)
                putExtra("contraseña", perfil.contraseña)
                putExtra("celular", perfil.celular)
                putExtra("pais", perfil.pais)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun areAllFieldsFilled(): Boolean {
        val fields = listOf<EditText>(
            binding.Nombre1,
            binding.Email1,
            binding.Password1,
            binding.Celular1,
            binding.Pais1
        )

        for (field in fields) {
            val text = field.text.toString().trim()
            if (text.isEmpty()) {
                field.requestFocus()
                Toast.makeText(this@EditarPerfilActivity, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}
