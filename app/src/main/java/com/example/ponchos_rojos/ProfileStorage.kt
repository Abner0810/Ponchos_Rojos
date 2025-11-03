package com.example.ponchos_rojos

import android.content.Context
import android.content.SharedPreferences

// guarda usuario localmente (solo 1)
data class UserProfile(
    val username: String = "",
    val nombre: String = "",
    val email: String = "",
    val contraseña: String = "",
    val celular: String = "",
    val pais: String = ""
)

class ProfileStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(profile: UserProfile) {
        prefs.edit().apply {
            putString(KEY_USERNAME, profile.username)
            putString(KEY_NOMBRE, profile.nombre)
            putString(KEY_EMAIL, profile.email)
            putString(KEY_CONTRASENA, profile.contraseña)
            putString(KEY_CELULAR, profile.celular)
            putString(KEY_PAIS, profile.pais)
            apply()
        }
    }

    fun load(): UserProfile {
        return UserProfile(
            username = prefs.getString(KEY_USERNAME, "") ?: "",
            nombre = prefs.getString(KEY_NOMBRE, "") ?: "",
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            contraseña = prefs.getString(KEY_CONTRASENA, "") ?: "",
            celular = prefs.getString(KEY_CELULAR, "") ?: "",
            pais = prefs.getString(KEY_PAIS, "") ?: ""
        )
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "user_profile_prefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_EMAIL = "email"
        private const val KEY_CONTRASENA = "contrasena"
        private const val KEY_CELULAR = "celular"
        private const val KEY_PAIS = "pais"
    }
}
