package com.example.ponchos_rojos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.GameInfo
import com.example.ponchos_rojos.R
import com.example.ponchos_rojos.activity_gameInfo
import com.example.ponchos_rojos.databinding.AdapterRecyclerLibraryBinding


class AdapterRecyclerLibrary(private val context: Context,private val imageList: MutableList<GameInfo>) :
    RecyclerView.Adapter<AdapterRecyclerLibrary.ImageViewHolder>() {
    inner class ImageViewHolder(private val binding: AdapterRecyclerLibraryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: GameInfo){
            binding.gameTitleLibrary.text = image.name
            val imageId = binding.root.context.resources.getIdentifier(image.imageName, "drawable", binding.root.context.packageName)
            if (imageId != 0) {
                binding.imageSample.setImageResource(imageId)
            }

            binding.imageSample.setOnClickListener {
                val intent = Intent(context, activity_gameInfo::class.java)
                intent.putExtra("gameData", image) // enviamos el objeto completo
                context!!.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        return ImageViewHolder(
            AdapterRecyclerLibraryBinding.inflate(
                //Layout Inflater = compila las vistas
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size
}