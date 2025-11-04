package com.example.ponchos_rojos.DataClass




// guarda usuario localmente (solo 1)
data class UserProfile(
    val username: String = "",
    val nombre: String = "",
    val email: String = "",
    val contraseña: String = "",
    val celular: String = "",
    val pais: String = ""
)