package com.example.listyzadan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listyzadan.databinding.ListyZadanBinding

class ListyZadanAdapter(private val listyZadan: MutableList<ExerciseList>,
    private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<ListyZadanAdapter.ListyZadanViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListyZadanViewHolder {
        return ListyZadanViewHolder(
            ListyZadanBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ) {index ->
            onItemClick(index)}
    }

    override fun getItemCount() = listyZadan.size

    override fun onBindViewHolder(holder: ListyZadanViewHolder, position: Int) {
        val currentItem = listyZadan[position]
        var count = 0

        fun calc() {
            for (i in 0..(listyZadan.size-1)) {
                if (currentItem.subject == listyZadan[i].subject) {
                    count++
                    if (i == position) {break}
                }
            }
        }

        calc()
        holder.bind(currentItem.subject.name, currentItem.exercises.size , count, currentItem.grade)
    }

    class ListyZadanViewHolder(private val binding: ListyZadanBinding,
        onItemClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                itemView.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        onItemClick(adapterPosition)
                    }
                }
            }
            fun bind(item: String, size: Int, listIndex: Int, grade: Float){
                binding.n1.text = item
                binding.n2.text = "Liczba zadań: $size"
                binding.n3.text = "Lista $listIndex"
                binding.n4.text = "Ocena: $grade"
            }
        }
}