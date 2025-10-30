package com.example.ponchos_rojos


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.databinding.ActivityEditarPerfilBinding

class EditarPerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPerfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flecha.setOnClickListener {
            finish()
        }

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.Nombre1.text.toString()
            val email = binding.Email1.text.toString()
            val contraseña = binding.Password1.text.toString()
            val celular = binding.Celular1.text.toString()
            val pais = binding.Pais1.text.toString()
            val resultIntent = Intent().apply {
                putExtra("nombre", nombre)
                putExtra("email", email)
                putExtra("contraseña", contraseña)
                putExtra("celular", celular)
                putExtra("pais", pais)
            }
            //se hizo cambios
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }
}