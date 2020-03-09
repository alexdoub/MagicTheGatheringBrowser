package com.example.magicthegathering.ui.cards.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magicthegathering.core.data.CardItem
import com.example.magicthegathering.core.network.MagicAPIClient
import kotlinx.coroutines.launch

class ListCardsViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<CardItem>>()
    val cards: LiveData<List<CardItem>>
        get() = _cards

    init {
        fetchCards()
    }

    private fun fetchCards() {
        viewModelScope.launch {
            val response = MagicAPIClient.getCards()
            _cards.value = response.cards
        }
    }
}
