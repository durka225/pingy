package com.example.pingy.api

import com.example.pingy.api.model.ChatItem
import com.example.pingy.api.model.ChatMessagesResponse
import com.example.pingy.api.model.CreateGroupChatRequest
import com.example.pingy.api.model.GroupChatDetailResponse
import com.example.pingy.api.model.GroupChatInfo
import com.example.pingy.api.model.GroupChatListItem
import com.example.pingy.api.model.GroupInviteCodeResponse
import com.example.pingy.api.model.GroupMemberDto
import com.example.pingy.api.model.LoginRequest
import com.example.pingy.api.model.MessageDto
import com.example.pingy.api.model.PresenceResponse
import com.example.pingy.api.model.RegisterProfileRequest
import com.example.pingy.api.model.RegisterRequest
import com.example.pingy.api.model.JoinGroupChatRequest
import com.example.pingy.api.model.SendMessageRequest
import com.example.pingy.api.model.TokenResponse
import com.example.pingy.api.model.UserProfileDto
import com.example.pingy.api.model.UserSearchItem
import com.example.pingy.api.model.UpdateGroupChatSettingsRequest
import com.example.pingy.api.model.UpdateGroupMemberRoleRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MessangerApi {
    // Auth
    @POST("/api/auth/register")
    suspend fun register(@Body body: RegisterRequest): TokenResponse

    @POST("/api/auth/register/profile")
    suspend fun registerProfile(@Body body: RegisterProfileRequest)

    @POST("/api/auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    // Users
    @GET("/api/users/me")
    suspend fun getMyProfile(): UserProfileDto

    @GET("/api/users/{username}")
    suspend fun getUserProfile(@Path("username") username: String): UserProfileDto

    @Multipart
    @POST("/api/users/me/avatar")
    suspend fun uploadMyAvatar(@Part file: MultipartBody.Part): UserProfileDto

    @GET("/api/users/search")
    suspend fun searchUsers(
        @Query("username") username: String,
        @Query("limit") limit: Int? = null,
    ): List<UserSearchItem>

    // Presence
    @POST("/api/presence/ping")
    suspend fun pingPresence()

    @GET("/api/presence/{username}")
    suspend fun getPresence(@Path("username") username: String): PresenceResponse

    // Chats
    @GET("/api/chats")
    suspend fun getMyChats(@Query("limit") limit: Int? = null): List<ChatItem>

    @GET("/api/chats/{username}/messages")
    suspend fun getChatMessages(
        @Path("username") username: String,
        @Query("limit") limit: Int? = null,
    ): ChatMessagesResponse

    @POST("/api/chats/{username}/messages")
    suspend fun sendMessage(
        @Path("username") username: String,
        @Body body: SendMessageRequest,
    ): MessageDto

    @Multipart
    @POST("/api/chats/{username}/messages/media")
    suspend fun sendMediaMessage(
        @Path("username") username: String,
        @Part file: MultipartBody.Part,
        @Query("text") text: String? = null,
        @Query("replyToMessageId") replyToMessageId: Long? = null,
    ): MessageDto

    // Group chats
    @GET("/api/group-chats")
    suspend fun getMyGroupChats(@Query("limit") limit: Int? = null): List<GroupChatListItem>

    @POST("/api/group-chats")
    suspend fun createGroupChat(@Body body: CreateGroupChatRequest): GroupChatInfo

    @GET("/api/group-chats/{chatId}")
    suspend fun getGroupChatDetail(
        @Path("chatId") chatId: String,
        @Query("limit") limit: Int? = null,
    ): GroupChatDetailResponse

    @GET("/api/group-chats/{chatId}/members")
    suspend fun getGroupMembers(@Path("chatId") chatId: String): List<GroupMemberDto>

    @POST("/api/group-chats/{chatId}/members")
    suspend fun addGroupMember(
        @Path("chatId") chatId: String,
        @Query("username") username: String,
    ): GroupMemberDto

    @DELETE("/api/group-chats/{chatId}/members/{username}")
    suspend fun removeGroupMember(
        @Path("chatId") chatId: String,
        @Path("username") username: String,
    ): List<GroupMemberDto>

    @PATCH("/api/group-chats/{chatId}/members/{username}/role")
    suspend fun updateGroupMemberRole(
        @Path("chatId") chatId: String,
        @Path("username") username: String,
        @Body body: UpdateGroupMemberRoleRequest,
    ): GroupMemberDto

    @PATCH("/api/group-chats/{chatId}/settings")
    suspend fun updateGroupSettings(
        @Path("chatId") chatId: String,
        @Body body: UpdateGroupChatSettingsRequest,
    ): GroupChatInfo

    @GET("/api/group-chats/{chatId}/invite-code")
    suspend fun getGroupInviteCode(@Path("chatId") chatId: String): GroupInviteCodeResponse

    @POST("/api/group-chats/join")
    suspend fun joinGroupChat(@Body body: JoinGroupChatRequest): GroupChatInfo

    @POST("/api/group-chats/{chatId}/messages")
    suspend fun sendGroupMessage(
        @Path("chatId") chatId: String,
        @Body body: SendMessageRequest,
    ): MessageDto

    @Multipart
    @POST("/api/group-chats/{chatId}/messages/media")
    suspend fun sendGroupMediaMessage(
        @Path("chatId") chatId: String,
        @Part file: MultipartBody.Part,
        @Query("text") text: String? = null,
        @Query("replyToMessageId") replyToMessageId: Long? = null,
    ): MessageDto
}
