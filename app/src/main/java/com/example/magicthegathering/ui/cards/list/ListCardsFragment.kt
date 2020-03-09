package com.example.magicthegathering.ui.cards.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.magicthegathering.core.network.MagicAPIClient
import com.example.magicthegathering.databinding.ListCardsFragmentBinding
import kotlinx.coroutines.launch

class ListCardsFragment : Fragment(), IOnItemClickedListener {

    companion object {
        fun newInstance() = ListCardsFragment()
    }

//    private lateinit var viewModel: ListCardsViewModel
    private val adapter = ListCardsAdapter(this)
    private lateinit var binding: ListCardsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListCardsFragmentBinding.inflate(LayoutInflater.from(context))
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCards()
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(ListCardsViewModel::class.java)
//        viewModel.cards.observe(viewLifecycleOwner, Observer { cards -> adapter.items = cards })
//    }

    override fun itemClicked(id: String) {

    }

    private fun fetchCards() {
        lifecycleScope.launch {
            val response = MagicAPIClient.getCards()
            adapter.items = response.cards
        }
    }
}
