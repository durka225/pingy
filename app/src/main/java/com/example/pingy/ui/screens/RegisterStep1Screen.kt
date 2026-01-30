package com.example.pingy.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pingy.ui.common.ToastEffect
import com.example.pingy.ui.preview.rememberPreviewAuthViewModel
import com.example.pingy.ui.theme.PingyTheme
import com.example.pingy.vm.AuthViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStep1Screen(
    vm: AuthViewModel,
    onNext: () -> Unit,
    onBackToLogin: () -> Unit,
) {
    ToastEffect(message = vm.errorText, onConsumed = vm::clearError)

    Scaffold(
//        topBar = {
//            TopAppBar(
//                navigationIcon = {
//                    IconButton(onClick = onBackToLogin) {
//                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Назад")
//                    }
//                },
//                title = {
//                    Text("Pingy")
//                        },
//            )
//        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(Modifier.height(13.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Назад",
                    modifier = Modifier.clickable { onBackToLogin() }.align(Alignment.TopStart).size(32.dp)
                )

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Регистрация", fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Шаг 1 из 2", fontSize = 16.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(117.dp))

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
                value = vm.username,
                onValueChange = { vm.username = it },
                placeholder = { Text("Придумайте логин", fontSize = 18.sp, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(32.dp)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next,
                ),
                shape = RoundedCornerShape(19.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(Modifier.height(9.dp))
            Text(text = "Только латиница и цифры", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))

            Spacer(Modifier.height(23.dp))

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
                value = vm.password,
                onValueChange = { vm.password = it },
                placeholder = { Text("Пароль", fontSize = 18.sp, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(32.dp)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                shape = RoundedCornerShape(19.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(21.dp))

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
                value = vm.passwordRepeat,
                onValueChange = { vm.passwordRepeat = it },
                placeholder = { Text("Повторите пароль", fontSize = 18.sp, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(32.dp)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                shape = RoundedCornerShape(19.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(192.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !vm.isLoading,
                onClick = { vm.registerStep1(onNext) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B7AED)
                )
            ) {
                Text("Далее", fontSize = 22.sp)
            }

            if (vm.isLoading) {
                Spacer(Modifier.height(16.dp))
                RowLoading()
            }

//            Spacer(Modifier.height(16.dp))
//            Text(
//                text = "Уже есть аккаунт? Войти",
//                color = MaterialTheme.colorScheme.primary,
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .clip(MaterialTheme.shapes.extraLarge)
//                    .clickable(onClick = onBackToLogin)
//                    .padding(horizontal = 12.dp, vertical = 8.dp),
//            )
        }
    }
}
@Composable
private fun RowLoading() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterStep1ScreenPreview() {
    val vm = rememberPreviewAuthViewModel()
    PingyTheme {
        RegisterStep1Screen(
            vm = vm,
            onNext = {},
            onBackToLogin = {},
        )
    }
}
