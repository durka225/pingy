package com.example.pingy.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.pingy.ui.common.ToastEffect
import com.example.pingy.ui.preview.rememberPreviewProfileViewModel
import com.example.pingy.ui.theme.PingyTheme
import com.example.pingy.util.MultipartUtils
import com.example.pingy.vm.ProfileViewModel

@SuppressLint("UseKtx")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    vm: ProfileViewModel,
    onDone: () -> Unit,
) {
    ToastEffect(message = vm.errorText, onConsumed = vm::clearError)

    val context = LocalContext.current

    val cropper = rememberLauncherForActivityResult(
        contract = CropImageContract(),
        onResult = { result ->
            if (!result.isSuccessful) return@rememberLauncherForActivityResult

            val uri = result.uriContent ?: return@rememberLauncherForActivityResult
            runCatching {
                val part = MultipartUtils.uriToCompressedImagePart(
                    context = context,
                    uri = uri,
                    partName = "file",
                    maxBytes = 500 * 1024,
                )

                val fileName = context.contentResolver
                    .query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                    ?.use { cursor ->
                        val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (idx == -1) return@use null
                        if (!cursor.moveToFirst()) return@use null
                        cursor.getString(idx)
                    } ?: "avatar.jpg"

                vm.setPendingAvatar(part, fileName)
                vm.setAvatarPreviewUri(uri.toString())
            }.onFailure {
                vm.clearPendingAvatar()
            }
        },
    )

    val avatarPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
            val options = CropImageOptions().apply {
                fixAspectRatio = true
                aspectRatioX = 1
                aspectRatioY = 1
                allowRotation = true
                allowFlipping = true
            }
            cropper.launch(CropImageContractOptions(uri = uri, cropImageOptions = options))
        },
    )

    Scaffold(
        //topBar = { TopAppBar(title = { Text("Pingy") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //ProfileHeader(previewUri = vm.selectedAvatarPreviewUri)
            Spacer(Modifier.height(13.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Назад",
                    modifier = Modifier.clickable {  }.align(Alignment.TopStart).size(32.dp)
                )

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "О себе", fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Шаг 2 из 2", fontSize = 16.sp, color = Color.Gray)
                }
            }
            Spacer(Modifier.height(20.dp))

            Surface(
                shape = CircleShape,
                color = Color(0xFFE8EBF2),
                tonalElevation = 2.dp,
            ) {
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(110.dp)
                        .clickable {
                            avatarPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (!vm.selectedAvatarPreviewUri.isNullOrBlank()) {
                        AsyncImage(
                            model = Uri.parse(vm.selectedAvatarPreviewUri),
                            contentDescription = null,
                            modifier = Modifier
                                .width(110.dp)
                                .height(110.dp)
                                .clip(CircleShape),
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = null,
                            modifier = Modifier.width(50.dp).height(50.dp),
                        )
                    }
                }
            }

            Spacer(Modifier.height(23.dp))

            Text(text = "Загрузить фото", fontSize = 20.sp)

            Spacer(Modifier.height(22.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    ),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                ),
                value = vm.displayName,
                onValueChange = { vm.displayName = it },
                placeholder = { Text("Как к вам обращаться", fontSize = 18.sp, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(32.dp)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = RoundedCornerShape(19.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(Modifier.height(39.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Text(text = "О себе", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 4.dp) )
            }


            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = false
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    ),
                value = vm.about,
                    onValueChange = { vm.about = it.take(120) },
                textStyle = TextStyle(
                    fontSize = 18.sp,
                ),
                placeholder = { Text("Пару слов о себе", fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold) },
                minLines = 5,
                shape = RoundedCornerShape(19.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(Modifier.height(80.dp))


//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp),
//                enabled = !vm.isLoading,
//                onClick = {
//                    avatarPicker.launch(
//                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                    )
//                },
//            ) {
//                Icon(Icons.Outlined.PhotoCamera, contentDescription = null)
//                Spacer(Modifier.width(10.dp))
//                Text("Загрузить аватар")
//            }
//            vm.selectedAvatarFileName?.let { name ->
//                Spacer(Modifier.height(8.dp))
//                Text(
//                    text = "Выбран файл: $name",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                )
//            }

            //Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !vm.isLoading,
                onClick = { vm.submit(onDone) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B7AED)
                )
            ) {
                Text("Создать аккаунт", fontSize = 22.sp)
            }

            if (vm.isLoading) {
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(previewUri: String?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 2.dp,
        ) {
            Box(
                modifier = Modifier.padding(18.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (!previewUri.isNullOrBlank()) {
                    AsyncImage(
                        model = Uri.parse(previewUri),
                        contentDescription = null,
                        modifier = Modifier
                            .width(52.dp)
                            .height(52.dp)
                            .clip(CircleShape),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.width(52.dp).height(52.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Регистрация — шаг 2",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Заполните профиль, чтобы завершить регистрацию",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val vm = rememberPreviewProfileViewModel()
    PingyTheme {
        ProfileScreen(
            vm = vm,
            onDone = {},
        )
    }
}
