package com.example.listyzadan

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listyzadan.databinding.FragmentE3Binding


class E3 : Fragment() {

    private lateinit var binding: FragmentE3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentE3Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val magic : Int = arguments?.getInt("magic")!!

        binding.recyclerView3.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView3.adapter = WidokListyAdapter(listyZadan, magic) {
            a, b ->
            var name = a.name
            binding.titleCard.text = "$name\nLista $b"
        }

    }
}