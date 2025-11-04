package com.example.ponchos_rojos

import android.content.Context
import com.example.ponchos_rojos.DataClass.User
import kotlinx.serialization.json.Json

// guarda todos los usuarios
object PrefAllUsers {
    private const val PREFS_NAME = "all_users_prefs"
    private const val KEY_USERS_MAP = "users_map"
    // configuración JSON
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    private fun loadMap(context: Context): MutableMap<String, User> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val s = prefs.getString(KEY_USERS_MAP, null) ?: return mutableMapOf()
        return try {
            json.decodeFromString<Map<String, User>>(s).toMutableMap()
        } catch (e: Exception) {
            mutableMapOf()
        }
    }

    private fun saveMap(context: Context, map: Map<String, User>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USERS_MAP, json.encodeToString(map)).apply()
    }

    fun saveUser(context: Context, user: User) {
        val map = loadMap(context)
        map[user.email] = user
        saveMap(context, map)
    }

    fun loadUserByEmail(context: Context, email: String): User? {
        val map = loadMap(context)
        return map[email]
    }

    fun removeUser(context: Context, email: String) {
        val map = loadMap(context)
        if (map.containsKey(email)) {
            map.remove(email)
            saveMap(context, map)
        }
    }

    fun replaceUserEmail(context: Context, oldEmail: String, newUser: User) {
        val map = loadMap(context)
        if (oldEmail != newUser.email) {
            // elimina antigua key si existe
            map.remove(oldEmail)
        }
        // inserta/actualiza nueva key
        map[newUser.email] = newUser
        saveMap(context, map)
    }
}
