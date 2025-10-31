package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameAdapter(private val context: Context, private val gameList: List<GameInfo>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameImageView: ImageView = itemView.findViewById(R.id.game_image)
        val gameNameTextView: TextView = itemView.findViewById(R.id.game_name)
        val gameTagsTextView: TextView = itemView.findViewById(R.id.game_tags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_card, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]

        holder.gameNameTextView.text = game.name
        holder.gameTagsTextView.text = game.tags.joinToString(", ")

        val imageId = context.resources.getIdentifier(
            game.imageName, "drawable", context.packageName
        )
        if (imageId != 0) {
            holder.gameImageView.setImageResource(imageId)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, activity_gameInfo::class.java)
            intent.putExtra("gameData", game) // enviamos el objeto completo
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return gameList.size
    }
}