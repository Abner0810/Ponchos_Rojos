package com.example.ponchos_rojos

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.DataClass.User
import com.example.ponchos_rojos.DataClass.UserProfile
import com.example.ponchos_rojos.databinding.CrearCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: CrearCuentaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: ProfileStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CrearCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storage = ProfileStorage(this)

        binding.botoncrear.setOnClickListener {

            if (!areAllFieldsFilled()) return@setOnClickListener

            val username = binding.nombreUsuario.text.toString().trim()
            val password = binding.contraseA.text.toString()
            val email = binding.email.text.toString().trim()
            val nombre = binding.Nombre.text.toString().trim()
            val celular = binding.celular.text.toString().trim()
            val pais = binding.pais.text.toString().trim()

            // Validaciones básicas adicionales
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val existingLocal = PrefAllUsers.loadUserByEmail(this, email)
            if (existingLocal != null) {
                Toast.makeText(this, "Este correo ya existe en el dispositivo. Intenta iniciar sesión.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // UX: deshabilitar el botón mientras se hace la petición
            binding.botoncrear.isEnabled = false

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    binding.botoncrear.isEnabled = true

                    if (task.isSuccessful) {
                        // Usuario creado y logueado
                        val firebaseUid = auth.currentUser?.uid ?: ""

                        // Preparar objetos locales (ahora con username)
                        val user = User(
                            username = username,
                            nombre = if (nombre.isNotEmpty()) nombre else username,
                            email = email,
                            contraseña = password,
                            celular = celular,
                            pais = pais
                        )

                        PrefAllUsers.saveUser(this, user)

                        // Guardar perfil local incluyendo username
                        val perfil = UserProfile(
                            username = user.username,
                            nombre = user.nombre,
                            email = user.email,
                            contraseña = user.contraseña,
                            celular = user.celular,
                            pais = user.pais
                        )
                        storage.save(perfil)

                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, TiendaActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        // Manejo específico: si Firebase dice que el email ya existe, avisar
                        val ex = task.exception
                        if (ex is FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "El correo ya está en uso. Intenta iniciar sesión o usa otro correo.", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Registro fallido: ${ex?.message ?: "Error"}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

        binding.volverlogin.setOnClickListener { finish() }
    }

    private fun areAllFieldsFilled(): Boolean {
        val fields = listOf<EditText>(
            binding.nombreUsuario,
            binding.contraseA,
            binding.email,
            binding.Nombre,
            binding.celular,
            binding.pais
        )

        for (field in fields) {
            val text = field.text.toString().trim()
            if (text.isEmpty()) {
                field.requestFocus()
                Toast.makeText(this@RegisterActivity, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}
