package com.example.pingy.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingy.api.model.ChatItem
import com.example.pingy.api.model.UserProfileDto
import com.example.pingy.api.model.UserSearchItem
import com.example.pingy.auth.TokenStore
import com.example.pingy.data.MessangerRepository
import kotlinx.coroutines.launch

class ChatsViewModel(
    private val repository: MessangerRepository,
    private val tokenStore: TokenStore,
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorText by mutableStateOf<String?>(null)
        private set

    fun clearError() {
        errorText = null
    }

    var chats by mutableStateOf<List<ChatItem>>(emptyList())
        private set

    var myProfile by mutableStateOf<UserProfileDto?>(null)
        private set

    var isProfileLoading by mutableStateOf(false)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var searchResults by mutableStateOf<List<UserSearchItem>>(emptyList())
        private set

    var isSearchLoading by mutableStateOf(false)
        private set

    fun refresh() {
        if (isLoading) return
        clearError()

        viewModelScope.launch {
            isLoading = true
            try {
                chats = repository.getMyChats(limit = 50)
            } catch (t: Throwable) {
                errorText = t.message ?: "Ошибка загрузки чатов"
            } finally {
                isLoading = false
            }
        }
    }

    fun onSearchQueryChanged(value: String) {
        searchQuery = value
        val q = value.trim()
        if (q.isEmpty()) {
            searchResults = emptyList()
            return
        }
        search(q)
    }

    private fun search(query: String) {
        if (isSearchLoading) return
        clearError()

        viewModelScope.launch {
            isSearchLoading = true
            try {
                searchResults = repository.searchUsers(usernameQuery = query, limit = 50)
            } catch (t: Throwable) {
                errorText = t.message ?: "Ошибка поиска"
            } finally {
                isSearchLoading = false
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            tokenStore.clearAuth()
            onDone()
        }
    }

    fun loadMyProfile() {
        if (isProfileLoading) return
        clearError()

        viewModelScope.launch {
            isProfileLoading = true
            try {
                myProfile = repository.getMyProfile()
            } catch (t: Throwable) {
                errorText = t.message ?: "Ошибка загрузки профиля"
            } finally {
                isProfileLoading = false
            }
        }
    }
}
