package com.example.ponchos_rojos

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.MediaController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ponchos_rojos.databinding.ActivityGameInfoBinding
import com.example.ponchos_rojos.databinding.ActivityLoginBinding

class activity_gameInfo : AppCompatActivity() {

    private lateinit var binding: ActivityGameInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGameInfoBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)

        //setContentView(R.layout.activity_game_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val mediaController = MediaController(this)
//        mediaController.setAnchorView(binding.eldenringVideo)


        val hideDelay = 3000L

        fun showButtonTemporarily(buttonToShow: ImageButton) {
            buttonToShow.visibility = View.VISIBLE

            buttonToShow.postDelayed({
                buttonToShow.visibility = View.GONE
            }, hideDelay)
        }

        val uri = Uri.parse("android.resource://" + "com.example.ponchos_rojos" + "/" + R.raw.elden_ring_compressed)
        binding.eldenringVideo.setVideoURI(uri)
        //binding.eldenringVideo.setMediaController(mediaController)
        // Play button
                binding.playButton.setOnClickListener {
                    binding.eldenringVideo.start()
                    binding.playButton.visibility = View.GONE      // se oculta inmediatamente
                    binding.pauseButton.visibility = View.VISIBLE  // aparece pause
                    showButtonTemporarily(binding.pauseButton)     // desaparece después de hideDelay
                }

// Pause button
        binding.pauseButton.setOnClickListener {
            binding.eldenringVideo.pause()
            binding.pauseButton.visibility = View.GONE     // se oculta inmediatamente
            binding.playButton.visibility = View.VISIBLE   // aparece play
            showButtonTemporarily(binding.playButton)      // desaparece después de hideDelay
        }

        binding.eldenringVideo.setOnTouchListener { v, _ ->
            v.performClick()
            if (binding.eldenringVideo.isPlaying) {
                // Si está reproduciendo, mostramos el botón de pausa
                binding.pauseButton.visibility = View.VISIBLE
                binding.playButton.visibility = View.GONE
                showButtonTemporarily(binding.pauseButton)
            } else {
                // Si está pausado, mostramos el botón de play
                binding.playButton.visibility = View.VISIBLE
                binding.pauseButton.visibility = View.GONE
                showButtonTemporarily(binding.playButton)
            }
            true
        }

        binding.addToCartButton.setOnClickListener {
            var ver = false

            binding.addToCartButton.setOnClickListener {
                if (ver) {
                    binding.addToCartButton.setBackgroundColor(ContextCompat.getColor(this, R.color.skyBlue))
                    binding.addToCartButton.text = "Added to your Cart"
                } else {
                    binding.addToCartButton.setBackgroundColor(ContextCompat.getColor(this, R.color.grayWhite))
                    binding.addToCartButton.text = "Add to Cart"

                }
                ver = !ver
            }
        }




    }
}