package com.example.magicthegathering.core.network

import com.example.magicthegathering.core.data.CardItem
import com.example.magicthegathering.core.data.CardsResponse
import retrofit2.http.GET

/**
 * Created by Alex Doub on 3/8/2020.
 */

interface MagicAPI {
    @GET("cards")
    suspend fun getCards(): CardsResponse
}