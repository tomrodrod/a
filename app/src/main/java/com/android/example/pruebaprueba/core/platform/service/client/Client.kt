package com.android.example.pruebaprueba.core.platform.service.client

import com.android.example.pruebaprueba.BuildConfig
import com.android.example.pruebaprueba.core.platform.service.client.UnsafeClient.setUnsafeClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.math.BigInteger
import java.security.MessageDigest

object Client {

    internal fun createClient() = runCatching {

        OkHttpClient.Builder().apply {

            setUnsafeClient(this)

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            this.addInterceptor(loggingInterceptor)
            addInterceptor { chain ->
                val currentTimestamp = System.currentTimeMillis()
                val newUrl = chain.request().url
                    .newBuilder()
                    .addQueryParameter("Authorization", "${BuildConfig.API_TOKEN}")
                    .build()

                val newRequest = chain.request()
                    .newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            }
        }
    }.map {
        it.build()
    }
        .getOrNull()
}
