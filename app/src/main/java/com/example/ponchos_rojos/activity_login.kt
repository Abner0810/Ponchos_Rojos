package com.example.ponchos_rojos
import android.content.Context
import android.content.Intent
import android.os.Bundle import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ponchos_rojos.DataClass.UserProfile
import com.example.ponchos_rojos.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class activity_login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

    val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize Firebase Auth
        auth = Firebase.auth
        binding.registerButtonText.setOnClickListener {
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener{
            val correo = binding.enterEmail.text.toString()
            val pass = binding.enterPassword.text.toString()

            if(correo!= "" && pass != "") {
                loginValidation(correo,pass)
            } else {
                Toast.makeText(
                    baseContext,
                    "debe ingresar un correo y contraseña",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            if(pass.length <8){
                Toast.makeText(
                    baseContext,
                    "La contraseña debe tener mas de 8 caractyeres" ,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intentPantallaPrincipal = Intent(context, TiendaActivity::class.java)
            startActivity(intentPantallaPrincipal)
        }
    }

    fun loginValidation(
        correo: String,
        pass:String
    ){
        auth.signInWithEmailAndPassword(correo,pass).addOnCompleteListener{ task->
            if(task.isSuccessful){
                // Cargar perfil y continuar
                loadProfileAndProceed(correo)
            }else{
                // el usuario no se pudo logear
                Toast.makeText(
                    baseContext,
                    "No se pudo logear al usuario",
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
    }

    // Carga el perfil desde PrefAllUsers y guarda en ProfileStorage (incluye username)
    private fun loadProfileAndProceed(email: String) {
        val localUser = PrefAllUsers.loadUserByEmail(this, email)
        val perfil = if (localUser != null) {
            UserProfile(
                username = localUser.username,
                nombre = localUser.nombre,
                email = localUser.email,
                contraseña = localUser.contraseña,
                celular = localUser.celular,
                pais = localUser.pais
            )
        } else {
            UserProfile(
                username = "",
                nombre = "",
                email = email,
                contraseña = "",
                celular = "",
                pais = ""
            )
        }
        ProfileStorage(this).save(perfil)
        val intentLogin = Intent(context, TiendaActivity::class.java)
        startActivity(intentLogin)
    }
}
