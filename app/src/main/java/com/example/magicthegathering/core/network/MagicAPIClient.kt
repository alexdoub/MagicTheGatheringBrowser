package com.example.magicthegathering.core.network

import com.example.magicthegathering.core.data.CardResponse
import com.example.magicthegathering.core.data.CardsResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Alex Doub on 3/8/2020.
 */

object MagicAPIClient {
    private val api: MagicAPI

    init {
        // Build API
        val gsonBuilder = GsonBuilder()
            .create()
        val gsonConverterFactory = GsonConverterFactory
            .create(gsonBuilder)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://api.magicthegathering.io/v1/")
            .addConverterFactory(gsonConverterFactory)
            .client(client)
            .build()
        api = retrofit.create(MagicAPI::class.java)
    }

    suspend fun getCards(): CardsResponse {
        return api.getCards()
    }

    suspend fun getCard(id: String): CardResponse {
        return api.getCard(id)
    }
}