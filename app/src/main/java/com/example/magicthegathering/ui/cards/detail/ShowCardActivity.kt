package com.example.magicthegathering.ui.cards.detail

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.magicthegathering.R
import com.example.magicthegathering.core.data.CardItem
import com.example.magicthegathering.core.network.MagicAPIClient
import com.example.magicthegathering.databinding.ShowCardFragmentOrActivityBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

/**
 * Created by Alex Doub on 3/11/2020.
 */

class ShowCardActivity : AppCompatActivity() {

    companion object {
        fun start(parent: Activity, cardItem: CardItem) {
            val intent = Intent(parent, ShowCardActivity::class.java)
            intent.putExtra("OBJ", Gson().toJson(cardItem))
            parent.startActivity(intent)
        }
    }

    lateinit var binding: ShowCardFragmentOrActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.show_card_fragment_or_activity)
        binding.root.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }
        loadFromArgs()
        title = "Details"
    }

    fun loadFromArgs() {

        val argJson = intent.getStringExtra("OBJ")
        val item = Gson().fromJson(argJson, CardItem::class.java)
        setData(item)
        fetchData(item.id)
    }

    fun fetchData(id: String) {
        lifecycleScope.launch {
            val response = MagicAPIClient.getCard(id)
            setData(response.card)
        }
    }

    private fun setData(cardItem: CardItem) {
        Picasso.get().load(cardItem.imageUrl).into(binding.image)
        binding.name.text = cardItem.name
        binding.cardText.text = cardItem.originalText
        binding.manaCost.text = cardItem.manaCost
        binding.types.text = cardItem.types.joinToString()


        val color = when (cardItem.rarity) {
            "Common" -> Color.LTGRAY
            "Uncommon" -> Color.YELLOW
            "Rare" -> Color.BLUE
            "Mythic Rare" -> Color.RED
            else -> Color.GREEN
        }

        binding.name.setTextColor(color)
        binding.types.setTextColor(color)
        binding.cardText.setTextColor(color)
        binding.manaCost.setTextColor(color)

        binding.cmc.removeAllViews()
        for (x in 1..cardItem.cmc) {
            val manaIcon = resources.getDrawable(R.drawable.ic_mana, null)
            val imageView = ImageView(this)
            imageView.setImageDrawable(manaIcon)
            binding.cmc.addView(imageView)
        }
    }
}