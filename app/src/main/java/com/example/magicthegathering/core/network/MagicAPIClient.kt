package com.example.magicthegathering.core.network

import com.example.magicthegathering.core.data.CardItem
import com.example.magicthegathering.core.data.CardsResponse
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://api.magicthegathering.io/v1/")
            .addConverterFactory(gsonConverterFactory)
            .build()
        api = retrofit.create(MagicAPI::class.java)
    }

    suspend fun getCards(): CardsResponse {
        return api.getCards()
    }
}