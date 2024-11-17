package com.example.listyzadan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listyzadan.databinding.OcenyBinding

class OcenyAdapter(private val listyZadan: MutableList<ExerciseList>):
    RecyclerView.Adapter<OcenyAdapter.OcenyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OcenyViewHolder {
        return OcenyViewHolder(
            OcenyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = listyZadan.size

    override fun onBindViewHolder(holder: OcenyViewHolder, position: Int) {
        val currentItem = listyZadan[position]
        holder.bind()
    }

    class OcenyViewHolder(private val binding: OcenyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(){
            binding.b1.text = "a"
            binding.b2.text = "a"
            binding.b3.text = "a"
        }
    }
}