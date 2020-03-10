package com.example.magicthegathering.ui.cards.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.magicthegathering.R
import com.example.magicthegathering.core.network.MagicAPIClient
import com.example.magicthegathering.databinding.ListCardsFragmentBinding
import com.example.magicthegathering.ui.cards.detail.ShowCardFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.main_activity.view.*
import kotlinx.coroutines.launch

class ListCardsFragment : Fragment(), IOnItemClickedListener {

    companion object {
        fun newInstance() = ListCardsFragment()
    }

    private val adapter = ListCardsAdapter(this)
    private lateinit var binding: ListCardsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListCardsFragmentBinding.inflate(LayoutInflater.from(context))
        binding.swipe.setOnRefreshListener { fetchCards() }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCards()
    }

    override fun itemClicked(id: String) {

        val thatItem = adapter.items.find { it.id == id } ?: return

        parentFragmentManager.beginTransaction()
            .add(R.id.container, ShowCardFragment.newInstance(thatItem), "1")
            .addToBackStack(null)
            .commit()
    }

    private fun fetchCards() {
        lifecycleScope.launch {
            binding.swipe.isRefreshing = true
            try {
                val response = MagicAPIClient.getCards()
                adapter.items = response.cards
            } catch (e: Throwable) {
                Toast.makeText(context, "Error fetching from server: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.swipe.isRefreshing = false
            }
        }
    }
}
