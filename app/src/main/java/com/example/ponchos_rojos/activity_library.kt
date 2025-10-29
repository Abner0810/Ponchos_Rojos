package com.example.ponchos_rojos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ponchos_rojos.adapters.AdapterRecyclerLibrary
import com.example.ponchos_rojos.databinding.ActivityGameInfoBinding
import com.example.ponchos_rojos.databinding.ActivityLibraryBinding

class activity_library : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLibraryBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        //setContentView(R.layout.activity_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val images = listOf(
            R.drawable.beyond_two_souls,
            R.drawable.beyond_two_souls,
            R.drawable.beyond_two_souls,
            R.drawable.detroit_become_human,
            R.drawable.detroit_become_human,
            R.drawable.detroit_become_human


            )
        val adapter = AdapterRecyclerLibrary(images)
binding.recyclerGames.setHasFixedSize(true)
        binding.recyclerGames.layoutManager = GridLayoutManager(this, 3) // o LinearLayoutManager
        binding.recyclerGames.adapter = adapter
    }


}