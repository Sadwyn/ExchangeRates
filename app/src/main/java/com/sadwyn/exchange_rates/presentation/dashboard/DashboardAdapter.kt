package com.sadwyn.exchange_rates.presentation.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sadwyn.exchange_rates.R
import com.sadwyn.exchange_rates.databinding.ItemCurrencySearchBinding
import com.sadwyn.exchange_rates.databinding.ItemDashboardBinding

class DashboardAdapter :
    ListAdapter<DashboardDataItem, DashboardAdapter.DashboardViewHolder>(DiffCallback()) {

    fun submit(data: List<DashboardDataItem>) {
        submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val binding =
            ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DashboardViewHolder(private val binding: ItemDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val foregroundLayout = binding.foregroundLayout

        fun bind(item: DashboardDataItem) {
            binding.apply {
                titleTextView.text = item.title
                valueTextView.text = item.value
                costTextView.text = item.cost ?: ""
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<DashboardDataItem>() {
        override fun areItemsTheSame(
            oldItem: DashboardDataItem,
            newItem: DashboardDataItem
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: DashboardDataItem,
            newItem: DashboardDataItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}