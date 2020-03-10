package com.example.magicthegathering.ui.cards.detail

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.magicthegathering.R
import com.example.magicthegathering.core.data.CardItem
import com.example.magicthegathering.core.network.MagicAPIClient
import com.example.magicthegathering.databinding.ShowCardFragmentBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

/**
 * Created by Alex Doub on 3/8/2020.
 */

class ShowCardFragment : Fragment() {
    companion object {
        fun newInstance(cardItem: CardItem): ShowCardFragment {
            return ShowCardFragment().apply {
                arguments = bundleOf("OBJ" to Gson().toJson(cardItem))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    lateinit var binding: ShowCardFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ShowCardFragmentBinding.inflate(inflater, container, false)
        binding.root.setOnClickListener {
            Toast.makeText(context, "Tapped", Toast.LENGTH_SHORT).show()
        }
        loadFromArgs()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    fun loadFromArgs() {
        val argJson = requireArguments().getString("OBJ")
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
            val manaIcon = requireContext().resources.getDrawable(R.drawable.ic_mana)
            val imageView = ImageView(requireContext())
            imageView.setImageDrawable(manaIcon)
            binding.cmc.addView(imageView)
        }
    }
}