package com.ikerfah.thmanyah.data.remote

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkClient {
    fun <T> create(baseUrl: String, service: Class<T>): T {
        val logging = HttpLoggingInterceptor().apply {
            level =
                HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()
        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(service)
    }
}