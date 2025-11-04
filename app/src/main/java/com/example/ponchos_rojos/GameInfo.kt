package com.example.ponchos_rojos

import java.io.Serializable

@kotlinx.serialization.Serializable
data class GameInfo(
    val id: Int,
    val name: String,
    val developer:String,
    val releasedDate:String,
    val description:String,
    val url:String,
    val tags: List<String> = emptyList(),
    val imageName: String,
    val price: String,

    //especificaciones minimas
    val so:String,
    val processor:String,
    val memory:String,
    val graphics:String,
    val storage:String,

    var timePlayed: Long = 0, // Total de tiempo jugado en horas
    var lastPlayedDate: Long = 0, // Inicia en 0.


) : Serializable
