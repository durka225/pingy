package com.example.pingy.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Locale
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object MultipartUtils {

    fun uriToFilePart(
        context: Context,
        uri: Uri,
        partName: String = "file",
    ): MultipartBody.Part {
        val contentResolver = context.contentResolver

        val mime = contentResolver.getType(uri) ?: "application/octet-stream"
        val fileName = queryDisplayName(contentResolver = contentResolver, uri = uri) ?: "upload"

        val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: error("Не удалось прочитать файл")

        val requestBody = bytes.toRequestBody(mime.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }

    /**
     * Сжимает изображение до [maxBytes] (JPEG) и возвращает multipart-part.
     *
     * Важно:
     * - сервер принимает media как обычный файл; для фото отправляем JPEG
     * - minSdk = 31, можно использовать ImageDecoder
     */
    fun uriToCompressedImagePart(
        context: Context,
        uri: Uri,
        partName: String = "file",
        maxBytes: Int = 500 * 1024,
    ): MultipartBody.Part {
        val contentResolver = context.contentResolver

        val originalName = queryDisplayName(contentResolver = contentResolver, uri = uri)
        val baseName = (originalName ?: "image")
            .substringBeforeLast('.')
            .takeIf { it.isNotBlank() }
            ?: "image"

        val bytes = compressImageToJpegBytes(
            context = context,
            uri = uri,
            maxBytes = maxBytes,
        )

        val fileName = "$baseName.jpg"
        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }

    private fun compressImageToJpegBytes(
        context: Context,
        uri: Uri,
        maxBytes: Int,
    ): ByteArray {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val decoded = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.isMutableRequired = false
        }

        // Стартуем с разумного размера, чтобы не пытаться гнать 4K+ через качество.
        var bitmap = decoded
        bitmap = downscaleIfNeeded(bitmap, maxSide = 1920)

        // 1) Пробуем качеством
        var quality = 92
        var jpeg = bitmap.toJpeg(quality)
        while (jpeg.size > maxBytes && quality > 45) {
            quality -= 7
            jpeg = bitmap.toJpeg(quality)
        }
        if (jpeg.size <= maxBytes) return jpeg

        // 2) Если всё ещё больше 500KB — уменьшаем геометрию по шагам
        var scale = 0.85f
        var attempt = 0
        while (jpeg.size > maxBytes && attempt < 6) {
            val newW = (bitmap.width * scale).toInt().coerceAtLeast(320)
            val newH = (bitmap.height * scale).toInt().coerceAtLeast(320)
            if (newW == bitmap.width || newH == bitmap.height) break

            bitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, true)
            quality = (quality - 5).coerceAtLeast(38)
            jpeg = bitmap.toJpeg(quality)
            scale *= 0.88f
            attempt++
        }

        return jpeg
    }

    private fun downscaleIfNeeded(bitmap: Bitmap, maxSide: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val longest = maxOf(w, h)
        if (longest <= maxSide) return bitmap

        val scale = maxSide.toFloat() / longest.toFloat()
        val newW = (w * scale).toInt().coerceAtLeast(1)
        val newH = (h * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, newW, newH, true)
    }

    private fun Bitmap.toJpeg(quality: Int): ByteArray {
        val out = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality.coerceIn(0, 100), out)
        return out.toByteArray()
    }

    private fun queryDisplayName(
        contentResolver: android.content.ContentResolver,
        uri: Uri,
    ): String? {
        return runCatching {
            contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex == -1) return@use null
                    if (!cursor.moveToFirst()) return@use null
                    cursor.getString(nameIndex)
                }
        }.getOrNull()
    }
}
