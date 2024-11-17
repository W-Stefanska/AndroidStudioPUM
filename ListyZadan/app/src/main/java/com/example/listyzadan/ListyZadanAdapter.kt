package com.example.listyzadan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listyzadan.databinding.ListyZadanBinding

class ListyZadanAdapter(private val listyZadan: MutableList<ExerciseList>):
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
        holder.bind(currentItem.subject.name, currentItem.exercises.size , position+1 , currentItem.grade)
    }

    class ListyZadanViewHolder(private val binding: ListyZadanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, size: Int, listIndex: Int, grade: Float){
            binding.n1.text = item
            binding.n2.text = "Liczba zadań: $size"
            binding.n3.text = "Lista $listIndex"
            binding.n4.text = "Ocena: $grade"
        }
    }
}