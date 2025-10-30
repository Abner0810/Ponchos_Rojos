package com.example.ponchos_rojos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
            R.drawable.beyond_two_souls_image,
            R.drawable.heavy_rain_image,
            R.drawable.detroit_become_human_image,
            R.drawable.hollow_knigth_image,
            R.drawable.neva_game,
            R.drawable.celeste_image,
            R.drawable.aplague_tale_requiem_image,
            R.drawable.cup_head_image,
            R.drawable.control_image,
            R.drawable.cyberpunk_image,
            R.drawable.eldenring,
            R.drawable.witcher_image






            )
        val adapter = AdapterRecyclerLibrary(images)
binding.recyclerGames.setHasFixedSize(true)
        binding.recyclerGames.layoutManager = LinearLayoutManager(this) // o LinearLayoutManager
        binding.recyclerGames.adapter = adapter
    }


}