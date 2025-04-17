package com.sadwyn.exchange_rates.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sadwyn.exchange_rates.R
import com.sadwyn.exchange_rates.databinding.ItemCurrencySearchBinding

class CurrencyAdapter(val onItemAddClick: (String, String) -> Unit) :
    ListAdapter<CurrencyDataItem, CurrencyAdapter.CurrencyViewHolder>(DiffCallback()) {

    fun submit(data: List<CurrencyDataItem>) {
        submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ItemCurrencySearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CurrencyViewHolder(private val binding: ItemCurrencySearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CurrencyDataItem) {
            binding.apply {
                titleTextView.text = item.title
                valueTextView.text = item.value
                iconButton.setOnClickListener {
                    onItemAddClick(item.title, item.value)
                }
                iconButton.isEnabled = !item.isAdded
                iconButton.setImageResource(
                    if (item.isAdded) {
                        R.drawable.ic_checked
                    } else {
                        R.drawable.ic_add
                    }
                )
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<CurrencyDataItem>() {
        override fun areItemsTheSame(
            oldItem: CurrencyDataItem,
            newItem: CurrencyDataItem
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: CurrencyDataItem,
            newItem: CurrencyDataItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}