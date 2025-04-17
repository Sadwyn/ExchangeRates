package com.sadwyn.exchange_rates.presentation.dashboard

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sadwyn.exchange_rates.R
import com.sadwyn.exchange_rates.databinding.FragmentDashboardBinding
import com.sadwyn.exchange_rates.presentation.search.CurrencyAdapter
import com.sadwyn.exchange_rates.presentation.search.SearchScreenEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var adapter: DashboardAdapter

    private var isSwiping: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSwipeToDelete()
        observeState()
    }

    private fun initViews() {
        adapter = DashboardAdapter()
        binding.dashboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.dashboardRecyclerView.adapter = adapter
    }

    private fun initSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (isSwiping) {
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                    return
                }
                val position = viewHolder.adapterPosition
                val item = adapter.currentList[position]
                onItemSwiped(item)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                isSwiping = isCurrentlyActive
                val foregroundView =
                    (viewHolder as DashboardAdapter.DashboardViewHolder).foregroundLayout
                getDefaultUIUtil().onDraw(
                    c,
                    recyclerView,
                    foregroundView,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                val foregroundView =
                    (viewHolder as DashboardAdapter.DashboardViewHolder).foregroundLayout
                getDefaultUIUtil().clearView(foregroundView)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.dashboardRecyclerView)
    }

    private fun onItemSwiped(item: DashboardDataItem) {
        viewModel.removeFavoriteAsset(item)
    }


    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currenciesState.collectLatest {
                binding.emptyStateText.visibility =
                    if (it.isEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                adapter.submit(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.singleEventChannel.collectLatest { event ->
                when (event) {
                    is DashboardScreenState.Error -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}