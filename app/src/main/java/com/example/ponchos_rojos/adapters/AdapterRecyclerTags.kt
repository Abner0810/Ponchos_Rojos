package com.example.ponchos_rojos.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.TiendaActivity
import com.example.ponchos_rojos.databinding.AdapterRecyclerTagsBinding

class AdapterRecyclerTags(
    private val context: Context,
    private val tagsList: List<String>
) : RecyclerView.Adapter<AdapterRecyclerTags.TagsButtonViewHolder>()  {
    // ViewHolder
    inner class TagsButtonViewHolder(val binding: AdapterRecyclerTagsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: String) {
            binding.recyclerTag.text = tag
            binding.recyclerTag.setOnClickListener {
                // Mandamos el tag si le hacen click a la tiendaactivity
                val intent = Intent(context, TiendaActivity::class.java)

                intent.putExtra("TAG_FILTER", tag)

                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsButtonViewHolder {
        //context = parent.context
        val binding = AdapterRecyclerTagsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return TagsButtonViewHolder(binding)
    }

    override fun getItemCount(): Int = tagsList.size

    override fun onBindViewHolder(holder: TagsButtonViewHolder, position: Int) {
        val tagName = tagsList[position]
        holder.bind(tagName)

    }
}