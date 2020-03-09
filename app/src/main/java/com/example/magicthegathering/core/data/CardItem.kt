package com.example.magicthegathering.core.data

data class CardItem(
    val artist: String,
    val cmc: Int,
    val colorIdentity: List<String>,
    val colors: List<String>,
    val foreignNames: List<ForeignName>,
    val id: String,
    val imageUrl: String,
    val layout: String,
    val manaCost: String,
    val multiverseid: Int,
    val name: String,
    val names: List<String>,
    val number: String,
    val originalText: String,
    val originalType: String,
    val power: String,
    val printings: List<String>,
    val rarity: String,
    val rulings: List<Ruling>,
    val `set`: String,
    val subtypes: List<String>,
    val supertypes: List<String>,
    val text: String,
    val toughness: String,
    val type: String,
    val types: List<String>
)