package com.example.magicthegathering.core.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Alex Doub on 3/8/2020.
 */

class CardsResponse(
    @SerializedName("cards")
    val cards: List<CardItem>
)