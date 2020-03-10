package com.example.magicthegathering.ui.cards.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.magicthegathering.R
import com.example.magicthegathering.core.network.MagicAPIClient
import com.example.magicthegathering.databinding.ListCardsFragmentBinding
import com.example.magicthegathering.ui.cards.detail.ShowCardFragment
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class ListCardsFragment : Fragment(), IOnItemClickedListener {

    companion object {
        fun newInstance() = ListCardsFragment()
    }

    private val adapter = ListCardsAdapter(this)
    private lateinit var binding: ListCardsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_cards_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.rarities) {
            val rarities = adapter.items.groupBy { it.rarity }.mapValues { it.value.size }
            val raritiesString = rarities.map { "${it.key}: ${it.value}" }.joinToString(separator = "\n")
            AlertDialog.Builder(requireContext())
                .setTitle("Rarities")
                .setMessage(raritiesString)
                .setPositiveButton("Done") { a, b ->
                    a.dismiss()
                }
                .show()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
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
