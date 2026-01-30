package com.example.pingy.data

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
import com.example.pingy.api.model.UserProfileDto
import com.example.pingy.api.model.UserSearchItem
import com.example.pingy.api.model.UpdateGroupChatSettingsRequest
import com.example.pingy.api.model.UpdateGroupMemberRoleRequest
import okhttp3.MultipartBody

class MessangerRepository(
    private val api: MessangerApi,
) {
    suspend fun register(username: String, password: String): TokenResponse {
        return api.register(RegisterRequest(username = username, password = password))
    }

    suspend fun registerProfile(displayName: String, about: String) {
        api.registerProfile(
            RegisterProfileRequest(
                displayName = displayName,
                about = about,
            )
        )
    }

    suspend fun getMyProfile(): UserProfileDto {
        return api.getMyProfile()
    }

    suspend fun getUserProfile(username: String): UserProfileDto {
        return api.getUserProfile(username)
    }

    suspend fun uploadMyAvatar(file: MultipartBody.Part): UserProfileDto {
        return api.uploadMyAvatar(file)
    }

    suspend fun login(username: String, password: String): TokenResponse {
        return api.login(LoginRequest(username = username, password = password))
    }

    suspend fun searchUsers(usernameQuery: String, limit: Int? = null): List<UserSearchItem> {
        return api.searchUsers(username = usernameQuery, limit = limit)
    }

    suspend fun pingPresence() {
        api.pingPresence()
    }

    suspend fun getPresence(username: String): PresenceResponse {
        return api.getPresence(username)
    }

    suspend fun getMyChats(limit: Int? = null): List<ChatItem> {
        return api.getMyChats(limit)
    }

    suspend fun getChatMessages(username: String, limit: Int? = null): ChatMessagesResponse {
        return api.getChatMessages(username = username, limit = limit)
    }

    suspend fun sendMessage(toUsername: String, text: String, replyToMessageId: Long? = null): MessageDto {
        return api.sendMessage(
            username = toUsername,
            body = SendMessageRequest(text = text, replyToMessageId = replyToMessageId),
        )
    }

    suspend fun sendMediaMessage(
        toUsername: String,
        file: MultipartBody.Part,
        text: String? = null,
        replyToMessageId: Long? = null,
    ): MessageDto {
        return api.sendMediaMessage(
            username = toUsername,
            file = file,
            text = text,
            replyToMessageId = replyToMessageId,
        )
    }

    suspend fun getMyGroupChats(limit: Int? = null): List<GroupChatListItem> {
        return api.getMyGroupChats(limit)
    }

    suspend fun createGroupChat(
        title: String,
        memberAddPolicy: String? = null,
        inviteRotation: String? = null,
    ): GroupChatInfo {
        return api.createGroupChat(
            CreateGroupChatRequest(
                title = title,
                memberAddPolicy = memberAddPolicy,
                inviteRotation = inviteRotation,
            )
        )
    }

    suspend fun getGroupChatDetail(chatId: String, limit: Int? = null): GroupChatDetailResponse {
        return api.getGroupChatDetail(chatId = chatId, limit = limit)
    }

    suspend fun getGroupMembers(chatId: String): List<GroupMemberDto> {
        return api.getGroupMembers(chatId)
    }

    suspend fun addGroupMember(chatId: String, username: String): GroupMemberDto {
        return api.addGroupMember(chatId = chatId, username = username)
    }

    suspend fun removeGroupMember(chatId: String, username: String): List<GroupMemberDto> {
        return api.removeGroupMember(chatId = chatId, username = username)
    }

    suspend fun updateGroupMemberRole(chatId: String, username: String, role: String): GroupMemberDto {
        return api.updateGroupMemberRole(
            chatId = chatId,
            username = username,
            body = UpdateGroupMemberRoleRequest(role = role),
        )
    }

    suspend fun updateGroupSettings(
        chatId: String,
        memberAddPolicy: String? = null,
        inviteRotation: String? = null,
    ): GroupChatInfo {
        return api.updateGroupSettings(
            chatId = chatId,
            body = UpdateGroupChatSettingsRequest(
                memberAddPolicy = memberAddPolicy,
                inviteRotation = inviteRotation,
            ),
        )
    }

    suspend fun getGroupInviteCode(chatId: String): GroupInviteCodeResponse {
        return api.getGroupInviteCode(chatId)
    }

    suspend fun joinGroupChat(code: String): GroupChatInfo {
        return api.joinGroupChat(JoinGroupChatRequest(code = code))
    }

    suspend fun sendGroupMessage(
        chatId: String,
        text: String,
        replyToMessageId: Long? = null,
    ): MessageDto {
        return api.sendGroupMessage(
            chatId = chatId,
            body = SendMessageRequest(text = text, replyToMessageId = replyToMessageId),
        )
    }

    suspend fun sendGroupMediaMessage(
        chatId: String,
        file: MultipartBody.Part,
        text: String? = null,
        replyToMessageId: Long? = null,
    ): MessageDto {
        return api.sendGroupMediaMessage(
            chatId = chatId,
            file = file,
            text = text,
            replyToMessageId = replyToMessageId,
        )
    }
}
