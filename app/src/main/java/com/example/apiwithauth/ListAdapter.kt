package com.example.apiwithauth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.apiwithauth.databinding.ItemBinding

class ListAdapter(private val items: List<ApiClass>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ApiClass, position: Int) {
            val truePosition = position + 1
            binding.recyclerImage.load(item.url)
            binding.recyclerText.text = truePosition.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

}