package com.sadwyn.exchange_rates.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sadwyn.exchange_rates.R
import com.sadwyn.exchange_rates.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeState()
        viewModel.onViewCreated()
    }

    private fun initViews() {
        adapter = CurrencyAdapter {name, value->
            viewModel.addFavoriteAsset(name, value)
        }
        binding.currencyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.currencyRecyclerView.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currenciesState.collectLatest {
                adapter.submit(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.singleEventChannel.collectLatest { event ->
                when (event) {
                    is SearchScreenEvent.Error -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState.collectLatest { loading ->
                binding.progress.visibility = if (loading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}