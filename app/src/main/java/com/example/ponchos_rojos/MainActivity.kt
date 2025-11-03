package com.example.ponchos_rojos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ponchos_rojos.databinding.ActivityMainBinding
import androidx.activity.enableEdgeToEdge

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Inicializa binding y establece el layout ANTES de usar cualquier vista
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Usa binding.root (nunca nulo) para trabajar con los insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startActivity(Intent(this@MainActivity, activity_login::class.java))
    }
}

