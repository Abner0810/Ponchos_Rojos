package com.example.ponchos_rojos

data class GameInfo(
    val id: Int,
    val name: String,
    val price: Double = 0.0,
    val description: String = "",
    val tags: List<String>,
    val imageName: String
)
