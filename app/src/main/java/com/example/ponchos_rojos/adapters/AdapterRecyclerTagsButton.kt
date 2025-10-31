package com.example.ponchos_rojos.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.GameInfo
import com.example.ponchos_rojos.databinding.AdapterRecyclerTagsButtonsBinding

class AdapterRecyclerTagsButton(private val context: Context,
private val tagsList: List<String>): RecyclerView.Adapter<AdapterRecyclerTagsButton.TagsButtonViewHolder>()  {
    // ViewHolder
    inner class TagsButtonViewHolder(private val binding: AdapterRecyclerTagsButtonsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: String) {
            binding.recyclerTagButton.text = tag

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsButtonViewHolder {
        //context = parent.context
        return TagsButtonViewHolder(
            AdapterRecyclerTagsButtonsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TagsButtonViewHolder, position: Int) {
        holder.bind(tagsList[position])
    }

    override fun getItemCount(): Int = tagsList.size

}