package com.example.pingy.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.pingy.api.MessangerApi
import com.example.pingy.api.model.ChatItem
import com.example.pingy.api.model.ChatMessagesResponse
import com.example.pingy.api.model.CreateGroupChatRequest
import com.example.pingy.api.model.GroupChatDetailResponse
import com.example.pingy.api.model.GroupChatInfo
import com.example.pingy.api.model.GroupChatListItem
import com.example.pingy.api.model.GroupInviteCodeResponse
import com.example.pingy.api.model.GroupMemberDto
import com.example.pingy.api.model.JoinGroupChatRequest
import com.example.pingy.api.model.LoginRequest
import com.example.pingy.api.model.MessageDto
import com.example.pingy.api.model.PresenceResponse
import com.example.pingy.api.model.RegisterProfileRequest
import com.example.pingy.api.model.RegisterRequest
import com.example.pingy.api.model.SendMessageRequest
import com.example.pingy.api.model.TokenResponse
import com.example.pingy.api.model.UpdateGroupChatSettingsRequest
import com.example.pingy.api.model.UpdateGroupMemberRoleRequest
import com.example.pingy.api.model.UserProfileDto
import com.example.pingy.api.model.UserSearchItem
import com.example.pingy.auth.TokenStore
import com.example.pingy.data.MessangerRepository
import com.example.pingy.vm.AuthViewModel
import com.example.pingy.vm.ProfileViewModel
import okhttp3.MultipartBody

@Composable
fun rememberPreviewAuthViewModel(): AuthViewModel {
    val context = LocalContext.current
    return remember {
        AuthViewModel(
            repository = MessangerRepository(PreviewMessangerApi()),
            tokenStore = TokenStore(context),
        ).apply {
            username = "msvir"
            password = "password"
            passwordRepeat = "password"
        }
    }
}

@Composable
fun rememberPreviewProfileViewModel(): ProfileViewModel {
    return remember {
        ProfileViewModel(
            repository = MessangerRepository(PreviewMessangerApi()),
        ).apply {
            displayName = "Мария"
            about = "Люблю Kotlin и красивые интерфейсы"
        }
    }
}

private class PreviewMessangerApi : MessangerApi {
    override suspend fun register(body: RegisterRequest): TokenResponse {
        return TokenResponse(token = "preview-token")
    }

    override suspend fun registerProfile(body: RegisterProfileRequest) {
        // no-op
    }

    override suspend fun login(body: LoginRequest): TokenResponse {
        return TokenResponse(token = "preview-token")
    }

    override suspend fun getMyProfile(): UserProfileDto {
        throw NotImplementedError("Preview")
    }

    override suspend fun getUserProfile(username: String): UserProfileDto {
        throw NotImplementedError("Preview")
    }

    override suspend fun uploadMyAvatar(file: MultipartBody.Part): UserProfileDto {
        throw NotImplementedError("Preview")
    }

    override suspend fun searchUsers(username: String, limit: Int?): List<UserSearchItem> {
        throw NotImplementedError("Preview")
    }

    override suspend fun pingPresence() {
        // no-op
    }

    override suspend fun getPresence(username: String): PresenceResponse {
        throw NotImplementedError("Preview")
    }

    override suspend fun getMyChats(limit: Int?): List<ChatItem> {
        throw NotImplementedError("Preview")
    }

    override suspend fun getChatMessages(username: String, limit: Int?): ChatMessagesResponse {
        throw NotImplementedError("Preview")
    }

    override suspend fun sendMessage(username: String, body: SendMessageRequest): MessageDto {
        throw NotImplementedError("Preview")
    }

    override suspend fun sendMediaMessage(
        username: String,
        file: MultipartBody.Part,
        text: String?,
        replyToMessageId: Long?,
    ): MessageDto {
        throw NotImplementedError("Preview")
    }

    override suspend fun getMyGroupChats(limit: Int?): List<GroupChatListItem> {
        throw NotImplementedError("Preview")
    }

    override suspend fun createGroupChat(body: CreateGroupChatRequest): GroupChatInfo {
        throw NotImplementedError("Preview")
    }

    override suspend fun getGroupChatDetail(chatId: String, limit: Int?): GroupChatDetailResponse {
        throw NotImplementedError("Preview")
    }

    override suspend fun getGroupMembers(chatId: String): List<GroupMemberDto> {
        throw NotImplementedError("Preview")
    }

    override suspend fun addGroupMember(chatId: String, username: String): GroupMemberDto {
        throw NotImplementedError("Preview")
    }

    override suspend fun removeGroupMember(chatId: String, username: String): List<GroupMemberDto> {
        throw NotImplementedError("Preview")
    }

    override suspend fun updateGroupMemberRole(
        chatId: String,
        username: String,
        body: UpdateGroupMemberRoleRequest,
    ): GroupMemberDto {
        throw NotImplementedError("Preview")
    }

    override suspend fun updateGroupSettings(
        chatId: String,
        body: UpdateGroupChatSettingsRequest,
    ): GroupChatInfo {
        throw NotImplementedError("Preview")
    }

    override suspend fun getGroupInviteCode(chatId: String): GroupInviteCodeResponse {
        throw NotImplementedError("Preview")
    }

    override suspend fun joinGroupChat(body: JoinGroupChatRequest): GroupChatInfo {
        throw NotImplementedError("Preview")
    }

    override suspend fun sendGroupMessage(chatId: String, body: SendMessageRequest): MessageDto {
        throw NotImplementedError("Preview")
    }

    override suspend fun sendGroupMediaMessage(
        chatId: String,
        file: MultipartBody.Part,
        text: String?,
        replyToMessageId: Long?,
    ): MessageDto {
        throw NotImplementedError("Preview")
    }
}
