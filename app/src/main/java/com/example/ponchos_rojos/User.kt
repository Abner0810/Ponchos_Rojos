package com.example.ponchos_rojos

data class User(
    val id: String = "",
    val nombre: String,
    val email: String,
    val contraseña: String,
    val celular: String = "",
    val pais: String = ""
)