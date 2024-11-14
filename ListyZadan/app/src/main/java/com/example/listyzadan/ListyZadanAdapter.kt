package com.example.listyzadan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listyzadan.databinding.FragmentE1Binding
import com.example.listyzadan.databinding.ListyZadanBinding

class ListyZadanAdapter(private val listyZadan: MutableList<String>):
    RecyclerView.Adapter<ListyZadanAdapter.ListyZadanViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListyZadanViewHolder {
        return ListyZadanViewHolder(
            ListyZadanBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = listyZadan.size

    override fun onBindViewHolder(holder: ListyZadanViewHolder, position: Int) {
        val currentItem = listyZadan[position]
        holder.bind(currentItem)
    }

    class ListyZadanViewHolder(private val binding: ListyZadanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String){
            binding.singleWord.text = item
        }
    }
}