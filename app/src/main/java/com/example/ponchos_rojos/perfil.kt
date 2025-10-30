package com.example.ponchos_rojos
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.databinding.ActivityMainPerfilBinding

object UserSession {
    var nombre: String = ""
    var email: String = ""
    var contraseña: String = ""
    var celular: String = ""
    var pais: String = ""
    fun clear() {
        nombre = ""
        email = ""
        contraseña = ""
        celular = ""
        pais = ""
    }
}

class MainPerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainPerfilBinding
    //actualizar datos si se uso guardar
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

                    Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Volver
        binding.flecha.setOnClickListener {
            finish()
        }
        //Cambiar
        binding.boton.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java).apply {
                // Enviamos los valores actuales para que el editor los muestre
                putExtra("nombre", binding.nombre1.text.toString())
                putExtra("email", binding.email1.text.toString())
                putExtra("contraseña", binding.password1.text.toString())
                putExtra("celular", binding.celular1.text.toString())
                putExtra("pais", binding.pais1.text.toString())
            }
            editLauncher.launch(intent)
        }

        //Cerrar
        binding.boton1.setOnClickListener {
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show()
            // Ejemplo: redirigir a LoginActivity
            // startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
