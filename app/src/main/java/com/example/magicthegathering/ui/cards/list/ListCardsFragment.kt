package com.example.magicthegathering.ui.cards.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magicthegathering.R
import com.example.magicthegathering.core.data.CardsResponse
import com.example.magicthegathering.core.network.MagicAPIClient
import com.example.magicthegathering.databinding.ListCardsFragmentBinding
import com.example.magicthegathering.ui.cards.detail.ShowCardActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListCardsFragment : Fragment(), IOnItemClickedListener {

    companion object {
        fun newInstance() = ListCardsFragment()
    }

    private val adapter = ListCardsAdapter(this)
    private lateinit var binding: ListCardsFragmentBinding
    private val viewDisposables = CompositeDisposable()
    private var calls = ArrayList<Call<*>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListCardsFragmentBinding.inflate(LayoutInflater.from(context), container, false)
        binding.swipe.setOnRefreshListener { fetchCards() }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_cards_menu, menu)
        requireActivity().title = "Full Cards List"
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

        ShowCardActivity.start(requireActivity(), thatItem)

//        requireFragmentManager().beginTransaction()
//            .add(R.id.container, ShowCardFragment.newInstance(thatItem))
//            .addToBackStack(null)   // Add transaction to back stack. Allows for popping back to this one
//            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        calls.forEach { it.cancel() }
        viewDisposables.clear()
    }

    private fun fetchCards() {
        fetchCards_cr()
//        fetchCards_rx()
//        fetchCards_standard()
    }

    private fun fetchCards_cr() {

        lifecycleScope.launch {
            binding.swipe.isRefreshing = true
            try {
                val response = MagicAPIClient.getCards_cr()
                adapter.items = response.cards.filter { it.imageUrl != null }
                binding.swipe.isRefreshing = false
            } catch (e: Throwable) {
                if (e !is CancellationException) {
                    Toast.makeText(context, "Error fetching from server: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    binding.swipe.isRefreshing = false
                }
            }
        }
    }

    private fun fetchCards_rx() {

        binding.swipe.isRefreshing = true
        val fetchCards = MagicAPIClient.getCards_rx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                adapter.items = response.cards
                binding.swipe.isRefreshing = false
            }, { e ->
                Toast.makeText(context, "Error fetching from server: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                binding.swipe.isRefreshing = false
            })
        viewDisposables.addAll(fetchCards)
    }

    private fun fetchCards_standard() {

        binding.swipe.isRefreshing = true
        val call = MagicAPIClient.getCards_standard()
        call.enqueue(object : Callback<CardsResponse> {
            override fun onResponse(call: Call<CardsResponse>, response: Response<CardsResponse>) {
                response.body()?.let { body ->
                    adapter.items = body.cards
                    binding.swipe.isRefreshing = false
                } ?: run {
                    Toast.makeText(context, "Error fetching from server: Body missing", Toast.LENGTH_SHORT).show()
                    binding.swipe.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<CardsResponse>, t: Throwable) {
                if (call.isCanceled) return
                Toast.makeText(context, "Error fetching from server: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                binding.swipe.isRefreshing = false
            }
        })
        calls.add(call)
    }
}
