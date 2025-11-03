package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ponchos_rojos.databinding.ItemGameCardBinding

class GameAdapter(private val context: Context, private var gameList: List<GameInfo>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    inner class GameViewHolder(private val binding: ItemGameCardBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(game:GameInfo){
            binding.gameName.text = game.name
            binding.gamePrice.text = "$${game.price}"
            binding.gameTags.text = game.tags.joinToString(", ")

            val imageId = context.resources.getIdentifier(
                game.imageName, "drawable", context.packageName
            )
            if (imageId != 0) {
                binding.gameImage.setImageResource(imageId)
            }

            binding.gameImage.setOnClickListener {
                val intent = Intent(context, activity_gameInfo::class.java)
                intent.putExtra("gameData", game) // enviamos el objeto completo
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGameCardBinding.inflate(inflater, parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(gameList[position])
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    fun updateList(newList: List<GameInfo>) {
        gameList = newList
        notifyDataSetChanged() // Le dice al RecyclerView que los datos han cambiado y debe actualizarse.
    }
}