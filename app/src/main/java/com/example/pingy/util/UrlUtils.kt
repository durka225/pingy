package com.example.pingy.util

/**
 * Сервер возвращает относительные пути (например "/api/files/avatars/x.png").
 * На клиенте нужно превратить их в абсолютные URL через базовый адрес API.
 */
fun toAbsoluteUrl(baseUrl: String, urlOrPath: String?): String? {
    val value = urlOrPath?.trim()?.takeIf { it.isNotEmpty() } ?: return null

    // Уже абсолютный URL
    if (value.startsWith("http://", ignoreCase = true) || value.startsWith("https://", ignoreCase = true)) {
        return value
    }

    val base = baseUrl.trim().trimEnd('/')
    val path = value.trimStart('/')
    return "$base/$path"
}
