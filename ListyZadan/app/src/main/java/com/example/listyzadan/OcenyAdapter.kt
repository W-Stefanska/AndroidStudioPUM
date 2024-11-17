package com.example.listyzadan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listyzadan.databinding.OcenyBinding

class OcenyAdapter(private val rosemary: MutableList<Summary>):
    RecyclerView.Adapter<OcenyAdapter.OcenyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OcenyViewHolder {
        return OcenyViewHolder(
            OcenyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = rosemary.size

    override fun onBindViewHolder(holder: OcenyViewHolder, position: Int) {
        val currentItem = rosemary[position]
        holder.bind(currentItem.subject.name, currentItem.average, currentItem.appearances)
    }

    class OcenyViewHolder(private val binding: OcenyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sub: String, avg: Double, app: Int){
            binding.b1.text = sub
            binding.b2.text = "Ilość list: $app"
            binding.b3.text = "Średnia ocen: %.1f".format(avg)
        }
    }
}