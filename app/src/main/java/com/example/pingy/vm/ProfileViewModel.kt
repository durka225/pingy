package com.example.pingy.vm

import okhttp3.MultipartBody
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pingy.data.MessangerRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: MessangerRepository,
) : ViewModel() {

    var displayName by mutableStateOf("")
    var about by mutableStateOf("")

    var selectedAvatarFileName by mutableStateOf<String?>(null)
        private set

    var selectedAvatarPreviewUri by mutableStateOf<String?>(null)
        private set

    private var pendingAvatarPart: MultipartBody.Part? = null

    var isLoading by mutableStateOf(false)
        private set

    var errorText by mutableStateOf<String?>(null)
        private set

    fun clearError() {
        errorText = null
    }

    fun submit(onDone: () -> Unit) {
        if (isLoading) return
        clearError()

        val cleanAbout = about.trim()
        if (cleanAbout.length > 120) {
            errorText = "О себе: максимум 120 символов"
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                repository.registerProfile(
                    displayName = displayName.trim(),
                    about = cleanAbout,
                )

                // По правилам сервера: сначала register/profile, затем avatar upload.
                pendingAvatarPart?.let { part ->
                    repository.uploadMyAvatar(part)
                }

                onDone()
            } catch (t: Throwable) {
                errorText = t.message ?: "Ошибка сохранения профиля"
            } finally {
                isLoading = false
            }
        }
    }

    fun setPendingAvatar(part: MultipartBody.Part, fileName: String?) {
        pendingAvatarPart = part
        selectedAvatarFileName = fileName
        // Preview will be set separately from UI when using cropper.
    }

    fun setAvatarPreviewUri(uri: String?) {
        selectedAvatarPreviewUri = uri
    }

    fun clearPendingAvatar() {
        pendingAvatarPart = null
        selectedAvatarFileName = null
        selectedAvatarPreviewUri = null
    }
}
