package com.example.magicthegathering.ui.main

/**
 * Created by Alex Doub on 3/8/2020.
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.magicthegathering.core.data.CardItem
import com.example.magicthegathering.databinding.ListCardItemBinding
import com.squareup.picasso.Picasso

class ListCardsAdapter() : RecyclerView.Adapter<CardItemViewHolder>() {

    var items: List<CardItem> = emptyList()
        set(value) {
            field = value.subList(0, 1)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemViewHolder {
        return CardItemViewHolder(parent.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

}


class CardItemViewHolder(
    val context: Context,
    val binding: ListCardItemBinding = ListCardItemBinding.inflate(LayoutInflater.from(context))
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CardItem) {
        val b = Picasso.Builder(context)
        b.listener { picasso, uri, exception ->
            println("URI:$uri")
        }
        val p = b.build()
        p.load(item.imageUrl).into(binding.image)
    }
}