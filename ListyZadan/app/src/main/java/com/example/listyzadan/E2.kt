package com.example.listyzadan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.listyzadan.databinding.FragmentE1Binding
import com.example.listyzadan.databinding.FragmentE2Binding


class E2 : Fragment() {

    private lateinit var binding: FragmentE2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentE2Binding.inflate(inflater)
        return binding.root
    }

}