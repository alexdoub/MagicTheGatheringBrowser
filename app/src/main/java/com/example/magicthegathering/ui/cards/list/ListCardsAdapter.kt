package com.example.magicthegathering.ui.cards.list

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

interface IOnItemClickedListener {
    fun itemClicked(id: String)
}

class ListCardsAdapter(private val listener: IOnItemClickedListener) :
    RecyclerView.Adapter<CardItemViewHolder>() {

    var items: List<CardItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemViewHolder {
        return CardItemViewHolder(parent.context, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class CardItemViewHolder(
    context: Context,
    private val listener: IOnItemClickedListener,
    private val binding: ListCardItemBinding = ListCardItemBinding.inflate(LayoutInflater.from(context))
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CardItem) {
        Picasso.get().load(item.imageUrl).into(binding.image)

        binding.root.setOnClickListener {
            listener.itemClicked(item.id)
        }
    }
}