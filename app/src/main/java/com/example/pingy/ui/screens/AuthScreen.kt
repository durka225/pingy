package com.example.pingy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.pingy.ui.common.ToastEffect
import com.example.pingy.ui.preview.rememberPreviewAuthViewModel
import com.example.pingy.ui.theme.PingyTheme
import com.example.pingy.vm.AuthViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pingy.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    vm: AuthViewModel,
    onLoggedIn: () -> Unit,
    onOpenRegister: () -> Unit,
) {
    ToastEffect(message = vm.errorText, onConsumed = vm::clearError)

    Scaffold(
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
            Spacer(Modifier.height(139.dp))
            Text (
                text = "Pingy",
                fontSize = 48.sp,
                fontFamily = FontFamily(Font(R.font.dessert_script))
            )
            Spacer(Modifier.height(91.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.username,
                onValueChange = { vm.username = it },
                placeholder = { Text("Логин") },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(32.dp)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next,
                ),
                shape = RoundedCornerShape(17.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(18.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.password,
                onValueChange = { vm.password = it },
                placeholder = { Text("Пароль") },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(32.dp)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                shape = RoundedCornerShape(17.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(37.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                enabled = !vm.isLoading,
                onClick = {
                    vm.login(onLoggedIn)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B7AED)
                )
            ) {
                Text(
                    text = "Войти",
                    fontSize = 22.sp
                )
            }

            if (vm.isLoading) {
                Spacer(Modifier.height(16.dp))
                RowLoading()
            }

            Spacer(Modifier.height(43.dp))
            Text(
                text = "Нет аккаунта?",
                color = Color(0xFFABACB1),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(MaterialTheme.shapes.extraLarge)
                    //.padding(horizontal = 12.dp, vertical = 8.dp),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Зарегистрироваться",
                color = Color(0xFF406693),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .clickable(onClick = onOpenRegister)
                    //.padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=384dp,height=832dp,dpi=440")
@Composable
private fun AuthScreenPreview() {
    val vm = rememberPreviewAuthViewModel()
    PingyTheme {
        AuthScreen(
            vm = vm,
            onLoggedIn = {},
            onOpenRegister = {},
        )
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
