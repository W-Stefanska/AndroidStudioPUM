package com.example.listyzadan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listyzadan.databinding.WidokListyBinding

class WidokListyAdapter(private val listyZadan: MutableList<ExerciseList>, private val i: Int,
                        private val titleCardSet: (Subject, Int) -> Unit
):
    RecyclerView.Adapter<WidokListyAdapter.WidokListyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidokListyViewHolder {
        return WidokListyViewHolder(
            WidokListyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun getItemCount() = listyZadan[i].exercises.size

    override fun onBindViewHolder(holder: WidokListyViewHolder, position: Int) {
        val currentItem = listyZadan[i].exercises[position]

        var count = 0
        fun calc() {
            for (n in 0..(listyZadan.size - 1)) {
                if (listyZadan[n].subject == listyZadan[i].subject) {
                    count++
                    if (n == i) {
                        break
                    }
                }
            }
        }

        calc()
        titleCardSet(listyZadan[i].subject, count)

        holder.bind(currentItem.points, currentItem.content, position+1)
    }

    class WidokListyViewHolder(private val binding: WidokListyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pts: Int, con: String, pos: Int){
            binding.z1.text = "Ilość punktów: $pts"
            binding.z2.text = "Zadanie $pos"
            binding.z3.text = "$con"

        }
    }
}