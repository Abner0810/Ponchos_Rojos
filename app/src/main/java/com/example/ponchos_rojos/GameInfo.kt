package com.example.ponchos_rojos

import androidx.annotation.DrawableRes


data class GameInfo(
    val id: Int,
    val name: String,
    val tags: List<String>,
    val imageName: String,
    val price: String
)
