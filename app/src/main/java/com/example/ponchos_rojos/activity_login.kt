package com.example.ponchos_rojos

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        //setContentView(R.layout.activity_login)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.loginButton.setOnClickListener{
            val correo = binding.enterEmail.text.toString()
            val pass = binding.enterPassword.text.toString()

            if(correo!= "" && pass != ""){
                //crearUsuario(correo,pass)
                loginValidation(correo,pass)
            }else{
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
        //reload()
        }
    }

    fun crearUsuario(
        correo: String,
        pass:String
    ){
        auth.createUserWithEmailAndPassword(correo, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)

                    //almacenar los datos
                    val intentLogin = Intent(context, TiendaActivity::class.java)
                    startActivity(intentLogin)
                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    //mensaje temporal
//                    Toast.makeText(
//                        baseContext,
//                        "Authentication failed.",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                    updateUI(null)



                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

    }
    fun loginValidation(
        correo: String,
        pass:String
    ){
        auth.signInWithEmailAndPassword(correo,pass).addOnCompleteListener{ task->
            if(task.isSuccessful){
                //usuario logeado correctamente
                val intentLogin = Intent(context, TiendaActivity::class.java)
                startActivity(intentLogin)
            }else{
                //el usuario no se pudo logear
                Toast.makeText(
                    baseContext,
                    "No se pudo logear al usuario",
                    //control de tiempo en pantalla
                    //Toast.LENGTH_SHORT,
                    Toast.LENGTH_LONG,
                ).show()
            }

        }
    }
}