package com.example.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.Navigation
import com.example.login.databinding.FragmentLoginBinding

class Login : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uin = binding.userIn.text.toString()
        val pin = binding.pass1in.text.toString()
        binding.logButton.setOnClickListener {
            for (i in users) {
                if (uin == i.name) {
                    if (pin == i.password) {
                        val action = LoginDirections.toWelcomeScreen()
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                    else {
                        Toast.makeText(requireActivity(), "Nieprawidłowa nazwa użytkownika lub hasło", LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.regButton.setOnClickListener {
            val action = LoginDirections.toRegistration()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }
}