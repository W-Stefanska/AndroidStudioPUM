package com.example.listyzadan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

private val listyZadan by lazy { MutableList(50) {"a"} }

class ListyZadanAdapter(private val listyZadan: MutableList<String>):
    RecyclerView.Adapter<ListyZadanAdapter.ListyZadanViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListyZadanViewHolder {
        return ListyZadanViewHolder(
            ListyZadanItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = listyZadan.size

    override fun onBindViewHolder(holder: ListyZadanViewHolder, position: Int) {
        val currentItem = listyZadan[position]
        holder.bind(currentItem)
    }

}

class ListyZadanViewHolder(private val binding: ListyZadanItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String){
            binding.singleWord.text = item
        }
    }

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}