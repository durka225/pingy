package com.example.pingy.api.model

// Auth

data class RegisterRequest(
    val username: String,
    val password: String,
)

data class RegisterProfileRequest(
    val displayName: String,
    val about: String,
)

data class LoginRequest(
    val username: String,
    val password: String,
)

data class TokenResponse(
    val token: String,
)

// Users

data class UserSearchItem(
    val username: String,
    val displayName: String,
    val avatarUrl: String? = null,
)

data class UserProfileDto(
    val username: String,
    val displayName: String,
    val about: String,
    val avatarUrl: String? = null,
)

// Presence

data class PresenceResponse(
    val username: String,
    val lastSeenAt: String,
    val online: Boolean,
)

// Chats

data class ChatItem(
    val chatId: String,
    val username: String,
    val lastMessageText: String? = null,
    val lastMessageSentAt: String,
    val lastMessageSender: String,
)

data class ChatMessagesResponse(
    val username: String,
    val displayName: String,
    val lastSeenAt: String,
    val online: Boolean,
    val messages: List<MessageDto>,
)

data class MessageDto(
    val id: Long,
    val chatId: String,
    val chatWith: String? = null,
    val chatTitle: String? = null,
    val sender: String,
    val replyToMessageId: Long? = null,
    val text: String? = null,
    val kind: String? = null,
    val mediaUrl: String? = null,
    val mediaMime: String? = null,
    val mediaSize: Long? = null,
    val sentAt: String,
)

data class SendMessageRequest(
    val text: String,
    val replyToMessageId: Long? = null,
)

// Group chats

data class GroupChatListItem(
    val chatId: String,
    val title: String,
    val lastMessageText: String? = null,
    val lastMessageSentAt: String,
    val lastMessageSender: String,
)

data class GroupChatInfo(
    val chatId: String,
    val title: String,
    val memberAddPolicy: String,
    val inviteRotation: String,
    val myRole: String,
)

data class GroupMemberDto(
    val username: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val role: String,
    val joinedAt: String,
)

data class GroupChatDetailResponse(
    val chat: GroupChatInfo,
    val messages: List<MessageDto>,
    val members: List<GroupMemberDto>,
)

data class GroupInviteCodeResponse(
    val code: String,
    val rotation: String,
    val nextRotateAt: String,
)

data class CreateGroupChatRequest(
    val title: String,
    val memberAddPolicy: String? = null,
    val inviteRotation: String? = null,
)

data class UpdateGroupChatSettingsRequest(
    val memberAddPolicy: String? = null,
    val inviteRotation: String? = null,
)

data class UpdateGroupMemberRoleRequest(
    val role: String,
)

data class JoinGroupChatRequest(
    val code: String,
)
