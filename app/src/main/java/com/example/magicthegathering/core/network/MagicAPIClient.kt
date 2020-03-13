package com.example.magicthegathering.core.network

import com.example.magicthegathering.core.data.CardResponse
import com.example.magicthegathering.core.data.CardsResponse
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        api = retrofit.create(MagicAPI::class.java)
    }

    fun getCards_rx(): Single<CardsResponse> {
        return api.getCards_rx()
    }

    suspend fun getCards_cr(): CardsResponse {
        return api.getCards_cr()
    }

    fun getCards_standard(): Call<CardsResponse> {
        return api.getCards_standard()
    }

    suspend fun getCard(id: String): CardResponse {
        return api.getCard(id)
    }
}