package com.example.listyzadan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listyzadan.databinding.FragmentE1Binding

class E1 : Fragment() {

    private lateinit var binding: FragmentE1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentE1Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = ListyZadanAdapter(listyZadan) {
                val action = E1Directions.actionE1ToE3()
                Navigation.findNavController(requireView()).navigate(action)
            }
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}

