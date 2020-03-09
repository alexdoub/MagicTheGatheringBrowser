package com.example.magicthegathering.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.magicthegathering.R
import com.example.magicthegathering.core.data.CardItem
import com.example.magicthegathering.databinding.ListCardsFragmentBinding

class ListCardsFragment : Fragment() {

    companion object {
        fun newInstance() = ListCardsFragment()
    }

    private lateinit var viewModel: ListCardsViewModel
    private val adapter = ListCardsAdapter()
    private lateinit var binding: ListCardsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListCardsFragmentBinding.inflate(LayoutInflater.from(context))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListCardsViewModel::class.java)
        viewModel.cards.observe(viewLifecycleOwner, Observer { cards -> adapter.items = cards })
    }
}
