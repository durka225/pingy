package com.example.pingy.ui.common

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun ToastEffect(
    message: String?,
    onConsumed: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(message) {
        if (!message.isNullOrBlank()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            onConsumed()
        }
    }
}
