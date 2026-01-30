package com.example.pingy.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingy.auth.TokenStore
import com.example.pingy.data.MessangerRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: MessangerRepository,
    private val tokenStore: TokenStore,
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordRepeat by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    var errorText by mutableStateOf<String?>(null)
        private set

    fun clearError() {
        errorText = null
    }

    fun login(onSuccess: () -> Unit) {
        if (isLoading) return
        clearError()

        viewModelScope.launch {
            isLoading = true
            try {
                val cleanUsername = username.trim()
                val token = repository.login(cleanUsername, password).token
                tokenStore.setToken(token)
                tokenStore.setUsername(cleanUsername)
                onSuccess()
            } catch (t: Throwable) {
                errorText = t.message ?: "Ошибка логина"
            } finally {
                isLoading = false
            }
        }
    }

    fun registerStep1(onSuccess: () -> Unit) {
        if (isLoading) return
        clearError()

        val cleanUsername = username.trim()
        if (cleanUsername.isEmpty()) {
            errorText = "Введите логин"
            return
        }

        if (password.isBlank() || passwordRepeat.isBlank()) {
            errorText = "Заполните оба поля пароля"
            return
        }

        if (password != passwordRepeat) {
            errorText = "Пароли не совпадают"
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val token = repository.register(cleanUsername, password).token
                tokenStore.setToken(token)
                tokenStore.setUsername(cleanUsername)
                onSuccess()
            } catch (t: Throwable) {
                errorText = t.message ?: "Ошибка регистрации"
            } finally {
                isLoading = false
            }
        }
    }
}
