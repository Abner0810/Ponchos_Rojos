package com.example.ponchos_rojos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.GameInfo
import com.example.ponchos_rojos.activity_gameInfo
import com.example.ponchos_rojos.databinding.AdapterRecyclerLibraryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class AdapterRecyclerLibrary(
    private val context: Context,
    private var imageList: MutableList<GameInfo>) :
    RecyclerView.Adapter<AdapterRecyclerLibrary.ImageViewHolder>() {
    private var currentSortMode: String = "Sort by: Name"
        inner class ImageViewHolder(private val binding: AdapterRecyclerLibraryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: GameInfo){
            when (currentSortMode) {
                "Time Played" -> {
                    val hours = TimeUnit.MINUTES.toHours(image.timePlayed)
                    val minutes = image.timePlayed % 60
                    binding.gameTitleLibrary.text = "${image.name}: ${hours}h ${minutes}m"
                }
                "Date" -> {
                    val sdf = SimpleDateFormat(
                        "dd/MM/yyyy", Locale.getDefault()
                    )
                    val dateString = sdf.format(Date(image.lastPlayedDate))
                    binding.gameTitleLibrary.text = "${image.name} - Last played: $dateString"
                }
                else -> { // Por defecto (y para "Sort by: Name")
                    binding.gameTitleLibrary.text = image.name
                }
            }
            val imageId = binding.root.context.resources.getIdentifier(image.imageName, "drawable", binding.root.context.packageName)
            if (imageId != 0) {
                binding.imageSample.setImageResource(imageId)
            }

            binding.imageSample.setOnClickListener {
                val intent = Intent(context, activity_gameInfo::class.java)
                intent.putExtra("gameData", image) // enviamos el objeto completo
                context.startActivity(intent)
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
    fun updateList(newList: List<GameInfo>, sortMode: String) {
        imageList = newList.toMutableList()
        currentSortMode = sortMode
        notifyDataSetChanged() // Le dice al RecyclerView que los datos han cambiado y debe actualizarse.
    }

    override fun getItemCount(): Int = imageList.size
}