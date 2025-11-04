package com.example.ponchos_rojos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ponchos_rojos.DataClass.User
import com.example.ponchos_rojos.DataClass.UserProfile
import com.example.ponchos_rojos.databinding.ActivityEditarPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class EditarPerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPerfilBinding
    private lateinit var storage: ProfileStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var storedProfile: UserProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = ProfileStorage(this)
        auth = FirebaseAuth.getInstance()

        // Cargar perfil local
        storedProfile = storage.load()

        // PREFILL: username (id_usuario) y emails (gmail, Email1) y demás campos
        // usamos intent extras si vienen, o los valores guardados localmente
        binding.idUsuario.setText(intent.getStringExtra("username") ?: storedProfile.username)

        // --- LÍNEAS AÑADIDAS: asegurar que idUsuario sea editable y reciba foco ---
        binding.idUsuario.isEnabled = true
        binding.idUsuario.isFocusable = true
        binding.idUsuario.isFocusableInTouchMode = true
        binding.idUsuario.inputType = InputType.TYPE_CLASS_TEXT
        binding.idUsuario.setSelection(binding.idUsuario.text?.length ?: 0)
        // -----------------------------------------------------------------------

        binding.Nombre1.setText(intent.getStringExtra("nombre") ?: storedProfile.nombre)
        binding.gmail.setText(intent.getStringExtra("email") ?: storedProfile.email)
        binding.Email1.setText(intent.getStringExtra("email") ?: storedProfile.email)
        binding.Password1.setText(intent.getStringExtra("contraseña") ?: storedProfile.contraseña)
        binding.Celular1.setText(intent.getStringExtra("celular") ?: storedProfile.celular)
        binding.Pais1.setText(intent.getStringExtra("pais") ?: storedProfile.pais)

        // DESHABILITAR EDICIÓN DE LOS DOS EMAILS (gmail y Email1) para que no se puedan cambiar
        binding.gmail.isEnabled = false
        binding.gmail.isFocusable = false
        binding.Email1.isEnabled = false
        binding.Email1.isFocusable = false

        // Flecha para volver
        binding.flecha.setOnClickListener { finish() }

        // Guardar cambios
        binding.btnGuardar.setOnClickListener {
            if (!areAllFieldsFilled()) return@setOnClickListener

            val username = binding.idUsuario.text.toString().trim()

            // Forzamos que el email no cambie desde aquí (campo no editable)
            val perfilNuevo = UserProfile(
                username = username,
                nombre = binding.Nombre1.text.toString().trim(),
                email = storedProfile.email,
                contraseña = binding.Password1.text.toString(),
                celular = binding.Celular1.text.toString().trim(),
                pais = binding.Pais1.text.toString().trim()
            )

            val oldEmail = storedProfile.email

            // Construimos User local con username
            val userLocalCandidate = User(
                username = username,
                nombre = perfilNuevo.nombre,
                email = perfilNuevo.email,
                contraseña = perfilNuevo.contraseña,
                celular = perfilNuevo.celular,
                pais = perfilNuevo.pais
            )

            // Si no hay usuario Firebase, guardamos solo localmente
            val currentUser = auth.currentUser
            if (currentUser == null) {
                PrefAllUsers.saveUser(this, userLocalCandidate)
                storage.save(perfilNuevo)
                Toast.makeText(this, "Cambios guardados localmente (no hay usuario en Firebase).", Toast.LENGTH_LONG).show()
                setResultOkAndFinish(perfilNuevo)
                return@setOnClickListener
            }

            // Solo permitimos actualizar contraseña y datos no sensibles; email se mantiene
            val needsPasswordUpdate = perfilNuevo.contraseña.isNotEmpty() && perfilNuevo.contraseña != storedProfile.contraseña

            if (!needsPasswordUpdate) {
                // Actualizamos PrefAllUsers (reemplazando la entrada existente con la misma clave email)
                PrefAllUsers.replaceUserEmail(this, oldEmail, userLocalCandidate)
                storage.save(perfilNuevo)
                Toast.makeText(this, "Perfil guardado localmente.", Toast.LENGTH_SHORT).show()
                setResultOkAndFinish(perfilNuevo)
                return@setOnClickListener
            }

            // NUEVA LÓGICA: no hay reautenticación, solo verificación de longitud >= 8
            if (perfilNuevo.contraseña.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }



            // Intentar actualizar la contraseña directamente en Firebase
            currentUser.updatePassword(perfilNuevo.contraseña).addOnCompleteListener { passTask ->
                if (passTask.isSuccessful) {
                    // Actualizamos locales (PrefAllUsers y ProfileStorage). Email se mantiene como clave
                    PrefAllUsers.replaceUserEmail(this, oldEmail, userLocalCandidate)
                    storage.save(perfilNuevo)
                    Toast.makeText(this, "Perfil guardado y sincronizado con Firebase.", Toast.LENGTH_SHORT).show()
                    setResultOkAndFinish(perfilNuevo)
                } else {
                    val e = passTask.exception
                    when (e) {
                        is FirebaseAuthWeakPasswordException -> {
                            Toast.makeText(this, "Contraseña débil: ${e?.reason ?: "usa al menos 8 caracteres."}", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            // Mensaje genérico si Firebase requiere reautenticación o falla por otra razón
                            Toast.makeText(this, "Error al actualizar contraseña: ${e?.message ?: "operación fallida (posible reautenticación requerida)."}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }



    private fun areAllFieldsFilled(): Boolean {
        // Revisamos username y los demás campos obligatorios (email no es editable pero username sí)
        val fields = listOf<EditText>(
            binding.idUsuario,
            binding.Nombre1,
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

    private fun setResultOkAndFinish(perfil: UserProfile) {
        val resultIntent = Intent().apply {
            putExtra("username", perfil.username)
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
