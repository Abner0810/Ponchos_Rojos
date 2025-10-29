package com.example.ponchos_rojos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.R
import com.example.ponchos_rojos.databinding.AdapterRecyclerLibraryBinding


class AdapterRecyclerLibrary(private val imageList: List<Int>) :
    RecyclerView.Adapter<AdapterRecyclerLibrary.ImageViewHolder>() {
    private var context: Context? = null
    // 👇 Aquí defines el ViewHolder
    inner class ImageViewHolder(private val binding: AdapterRecyclerLibraryBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageItem: ImageView = binding.imageSample
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        context = parent.context
        return ImageViewHolder(
            AdapterRecyclerLibraryBinding.inflate(
                //Layout Inflater = compila las vistas
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageItem.setImageResource(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size
}