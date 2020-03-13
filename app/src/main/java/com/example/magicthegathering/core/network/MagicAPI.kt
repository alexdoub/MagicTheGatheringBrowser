package com.example.magicthegathering.core.network

import com.example.magicthegathering.core.data.CardItem
import com.example.magicthegathering.core.data.CardResponse
import com.example.magicthegathering.core.data.CardsResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Alex Doub on 3/8/2020.
 */

interface MagicAPI {

    @GET("cards")
    fun getCards_rx(): Single<CardsResponse>

    @GET("cardzs")
    suspend fun getCards_cr(): CardsResponse

    @GET("cards")
    fun getCards_standard(): Call<CardsResponse>

    @GET("cards/{id}")
    suspend fun getCard(@Path("id") id: String): CardResponse
}