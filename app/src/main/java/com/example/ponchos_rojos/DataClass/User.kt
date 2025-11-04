package com.example.ponchos_rojos.DataClass

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String = "",
    val nombre: String = "",
    val email: String = "",
    val contraseña: String = "",
    val celular: String = "",
    val pais: String = ""
)

