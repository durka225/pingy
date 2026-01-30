package com.example.pingy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pingy.R
import com.example.pingy.api.ApiClient
import com.example.pingy.ui.common.EmptyState
import com.example.pingy.ui.common.ToastEffect
import com.example.pingy.util.toAbsoluteUrl
import com.example.pingy.vm.ChatsViewModel

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    vm: ChatsViewModel,
    currentUsername: String?,
    onOpenSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenGroupChats: () -> Unit,
    onOpenChat: (String) -> Unit,
    onLoggedOut: () -> Unit,
    onOpenProfile: () -> Unit = {},
    onOpenMenu: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        vm.refresh()
    }

    ToastEffect(message = vm.errorText, onConsumed = vm::clearError)

    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Chats) }
    var isSearchActive by remember { mutableStateOf(false) }
    val showSearch = selectedTab == MainTab.Chats && (isSearchActive || vm.searchQuery.isNotBlank())

    LaunchedEffect(selectedTab) {
        if (selectedTab == MainTab.Profile && vm.myProfile == null) {
            vm.loadMyProfile()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onOpenMenu) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Меню")
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Pingy",
                    fontSize = 28.sp,
                    fontFamily = FontFamily(Font(R.font.dessert_script)),
                )
                Spacer(Modifier.weight(1f))
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE6F0FF),
                    tonalElevation = 0.dp,
                ) {
//                    IconButton(onClick = onOpenGroupChats) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Новый чат",
                            tint = Color(0xFF1B7AED),
                        )
//                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (selectedTab == MainTab.Chats) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .onFocusChanged { focusState ->
                            isSearchActive = focusState.isFocused
                        },
                    value = vm.searchQuery,
                    onValueChange = vm::onSearchQueryChanged,
                    placeholder = { Text("Поиск", color = Color(0xFF8E99A6)) },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Поиск") },
                    trailingIcon = {
                        if (vm.searchQuery.isNotBlank()) {
                            IconButton(onClick = { vm.onSearchQueryChanged("") }) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "Очистить")
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F4F8),
                        unfocusedContainerColor = Color(0xFFF1F4F8),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    singleLine = true,
                    enabled = true,
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                )

                Spacer(Modifier.height(8.dp))
            }

            if (selectedTab == MainTab.Chats && (vm.isLoading || vm.isSearchLoading)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                }
            }

            when (selectedTab) {
                MainTab.Chats -> {
                    ChatsTabContent(
                        vm = vm,
                        currentUsername = currentUsername,
                        showSearch = showSearch,
                        onOpenChat = onOpenChat,
                    )
                }
                MainTab.Profile -> {
                    ProfileTabContent(
                        profileName = vm.myProfile?.displayName.orEmpty(),
                        username = vm.myProfile?.username?.let { "@$it" }.orEmpty(),
                        about = vm.myProfile?.about.orEmpty(),
                        avatarUrl = vm.myProfile?.avatarUrl,
                        isLoading = vm.isProfileLoading,
                    )
                }
                MainTab.Settings -> {
                    SettingsTabContent(onLogout = { vm.logout(onLoggedOut) })
                }
            }
        }

    }
}

private fun formatChatTime(sentAtIso: String): String {
    return try {
        val zoneId = ZoneId.systemDefault()
        val instant = Instant.parse(sentAtIso)
        val date = instant.atZone(zoneId).toLocalDate()
        val today = LocalDate.now(zoneId)
        if (date == today) {
            DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId).format(instant)
        } else {
            val locale = Locale("ru")
            val formatter = DateTimeFormatter.ofPattern("EEE", locale)
            formatter.format(date).replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
        }
    } catch (_: Throwable) {
        ""
    }
}

@Composable
private fun ChatsTabContent(
    vm: ChatsViewModel,
    currentUsername: String?,
    showSearch: Boolean,
    onOpenChat: (String) -> Unit,
) {
    if (showSearch) {
        if (vm.searchQuery.isBlank() && !vm.isSearchLoading) {
            EmptyState(
                icon = Icons.Filled.Search,
                title = "Введите запрос",
                subtitle = "Начните вводить никнейм пользователя",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
            )
            return
        }

        if (vm.searchQuery.isNotBlank() && !vm.isSearchLoading && vm.searchResults.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Search,
                title = "Ничего не найдено",
                subtitle = "Попробуйте другой запрос",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
            )
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 8.dp,
                bottom = 12.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            items(vm.searchResults, key = { it.username }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onOpenChat(item.username) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE8EBF2),
                        modifier = Modifier.size(48.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val letter = item.displayName.firstOrNull()?.uppercaseChar()?.toString()
                                ?: item.username.firstOrNull()?.uppercaseChar()?.toString().orEmpty()
                            Text(text = letter, color = Color(0xFF5B6B7A))
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "@${item.username}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF8E99A6),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        return
    }

    if (!vm.isLoading && vm.chats.isEmpty()) {
        EmptyState(
            icon = Icons.Outlined.Chat,
            title = "Чатов пока нет",
            subtitle = "Напишите первое сообщение, чтобы создать диалог",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 8.dp,
            bottom = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(vm.chats, key = { it.chatId }) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onOpenChat(item.username) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE8EBF2),
                    modifier = Modifier.size(48.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        val letter = item.username.firstOrNull()?.uppercaseChar()?.toString().orEmpty()
                        Text(text = letter, color = Color(0xFF5B6B7A))
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = item.username,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    val prefix = if (!currentUsername.isNullOrBlank() && item.lastMessageSender == currentUsername) {
                        "Вы: "
                    } else {
                        ""
                    }
                    val lastText = item.lastMessageText?.trim().orEmpty()
                    Text(
                        text = prefix + lastText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF8E99A6),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatChatTime(item.lastMessageSentAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF8E99A6),
                    )
                    Spacer(Modifier.height(6.dp))
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF1B7AED),
                    ) {
                        Text(
                            text = " ",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(8.dp))
}

@Composable
private fun ProfileTabContent(
    profileName: String,
    username: String,
    about: String,
    avatarUrl: String?,
    isLoading: Boolean,
) {
    ProfileContent(
        profileName = profileName,
        username = username,
        about = about,
        avatarUrl = avatarUrl,
        isLoading = isLoading,
    )
}

@Composable
private fun SettingsTabContent(
    onLogout: () -> Unit,
) {
    SettingsContent(onLogout = onLogout)
}

@Composable
private fun BottomNavBar(
    modifier: Modifier = Modifier,
    selectedTab: MainTab,
    onSelectTab: (MainTab) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = Color(0xFFF1F4F8),
            tonalElevation = 2.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (selectedTab == MainTab.Profile) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFF1B7AED),
                    ) {
                        BottomNavItem(
                            icon = Icons.Outlined.Person,
                            label = "Профиль",
                            selected = true,
                            onClick = { onSelectTab(MainTab.Profile) },
                            selectedTextColor = Color.White,
                            selectedIconColor = Color.White,
                            paddingHorizontal = 18.dp,
                        )
                    }
                } else {
                    BottomNavItem(
                        icon = Icons.Outlined.Person,
                        label = "Профиль",
                        selected = false,
                        onClick = { onSelectTab(MainTab.Profile) },
                    )
                }

                if (selectedTab == MainTab.Chats) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFF1B7AED),
                    ) {
                        BottomNavItem(
                            icon = Icons.Outlined.Chat,
                            label = "Чаты",
                            selected = true,
                            onClick = { onSelectTab(MainTab.Chats) },
                            selectedTextColor = Color.White,
                            selectedIconColor = Color.White,
                            paddingHorizontal = 18.dp,
                        )
                    }
                } else {
                    BottomNavItem(
                        icon = Icons.Outlined.Chat,
                        label = "Чаты",
                        selected = false,
                        onClick = { onSelectTab(MainTab.Chats) },
                    )
                }

                if (selectedTab == MainTab.Settings) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFF1B7AED),
                    ) {
                        BottomNavItem(
                            icon = Icons.Outlined.Settings,
                            label = "Настройки",
                            selected = true,
                            onClick = { onSelectTab(MainTab.Settings) },
                            selectedTextColor = Color.White,
                            selectedIconColor = Color.White,
                            paddingHorizontal = 18.dp,
                        )
                    }
                } else {
                    BottomNavItem(
                        icon = Icons.Outlined.Settings,
                        label = "Настройки",
                        selected = false,
                        onClick = { onSelectTab(MainTab.Settings) },
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    selectedTextColor: Color = Color(0xFF1B7AED),
    selectedIconColor: Color = Color(0xFF1B7AED),
    paddingHorizontal: Dp = 8.dp,
) {
    val textColor = if (selected) selectedTextColor else Color(0xFF8E99A6)
    val iconColor = if (selected) selectedIconColor else Color(0xFF8E99A6)

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = paddingHorizontal, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = iconColor)
        Text(text = label, color = textColor, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun ProfileContent(
    profileName: String,
    username: String,
    about: String,
    avatarUrl: String?,
    isLoading: Boolean,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLoading) {
                Spacer(Modifier.height(24.dp))
                CircularProgressIndicator()
            }

            val absoluteAvatar = toAbsoluteUrl(ApiClient.BASE_URL, avatarUrl)

            Surface(
                shape = CircleShape,
                color = Color(0xFFE8EBF2),
                modifier = Modifier.size(96.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    when {
                        !absoluteAvatar.isNullOrBlank() -> {
                            AsyncImage(
                                model = absoluteAvatar,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clip(CircleShape),
                            )
                        }

                        else -> {
                            Image(
                                painter = painterResource(id = R.drawable.non_avatar),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clip(CircleShape),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = profileName.ifBlank { "..." },
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.height(35.dp))

            ProfileField(label = "Полное имя", value = profileName)
            ProfileField(label = "Имя пользователя", value = username)
            ProfileField(label = "О себе", value = about)
        }

        Surface(
            shape = CircleShape,
            color = Color(0xFF1B7AED),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
                .clickable { },
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Редактировать",
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = value.ifBlank { "—" },
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF8E99A6),
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFE8EBF2))
        Spacer(Modifier.height(13.dp))
    }
}

@Composable
private fun SettingsContent(
    onLogout: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFF56B6B),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
                .clickable { onLogout() },
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp,
                    contentDescription = "Выйти",
                    tint = Color.White,
                )
            }
        }
    }
}

private enum class MainTab {
    Chats,
    Profile,
    Settings,
}
