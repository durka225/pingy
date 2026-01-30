package com.example.pingy.api

import com.example.pingy.BuildConfig
import com.example.pingy.auth.AuthInterceptor
import com.example.pingy.auth.TokenStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiClient {
    // Важно: Retrofit требует, чтобы baseUrl заканчивался на '/'.
    // Здесь же держим единственный источник правды для склейки абсолютных URL (аватары/медиа).
    const val BASE_URL: String = BuildConfig.API_BASE_URL

    fun create(tokenStore: TokenStore, enableHttpLogging: Boolean = false): MessangerApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (enableHttpLogging) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStore))
            .addInterceptor(loggingInterceptor)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(MessangerApi::class.java)
    }
}
