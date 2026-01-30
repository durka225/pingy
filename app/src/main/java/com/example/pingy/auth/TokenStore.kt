package com.example.pingy.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "auth"

private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class TokenStore(private val appContext: Context) {
    private val tokenKey = stringPreferencesKey("jwt")
    private val usernameKey = stringPreferencesKey("username")

    fun tokenFlow(): Flow<String?> {
        return appContext.dataStore.data.map { prefs -> prefs[tokenKey] }
    }

    fun usernameFlow(): Flow<String?> {
        return appContext.dataStore.data.map { prefs -> prefs[usernameKey] }
    }

    suspend fun getToken(): String? {
        return tokenFlow().firstOrNull()
    }

    suspend fun setToken(token: String) {
        appContext.dataStore.edit { prefs ->
            prefs[tokenKey] = token
        }
    }

    suspend fun setUsername(username: String) {
        appContext.dataStore.edit { prefs ->
            prefs[usernameKey] = username
        }
    }

    suspend fun clearToken() {
        appContext.dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }

    suspend fun clearUsername() {
        appContext.dataStore.edit { prefs ->
            prefs.remove(usernameKey)
        }
    }

    suspend fun clearAuth() {
        appContext.dataStore.edit { prefs ->
            prefs.remove(tokenKey)
            prefs.remove(usernameKey)
        }
    }
}
