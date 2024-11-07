package com.example.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.Navigation
import com.example.login.databinding.FragmentRegistrationBinding

class Registration : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var username = binding.userIn.text.toString()
        fun compareUsernames(): Int {
            for (i in users) {
                if (username == i.name) {
                    return 1
                }
            }
            return 0
        }
        fun check(): Int {
            username = binding.userIn.text.toString()
            val password = binding.pass1in.text.toString()
            val password2 = binding.pass2in.text.toString()
            return when {
                username.isEmpty() -> 1
                password.isEmpty() || password2.isEmpty() -> 2
                password != password2 -> 3
                compareUsernames() == 1 -> 4
                else -> 0
            }
        }
        val toast1 = Toast.makeText(requireActivity(), "Nazwa użytkownika nie może być pusta", LENGTH_SHORT)
        val toast2 = Toast.makeText(requireActivity(), "Hasło nie może być puste", LENGTH_SHORT)
        val toast3 = Toast.makeText(requireActivity(), "Hasła muszą się zgadzać", LENGTH_SHORT)
        val toast4 = Toast.makeText(requireActivity(), "Nazwa użytkownika jest już zajęta", LENGTH_SHORT)
        val action = RegistrationDirections.toLogin()
        binding.regButton.setOnClickListener {
            when (check()) {
                1 -> toast1.show()
                2 -> toast2.show()
                3 -> toast3.show()
                4 -> toast4.show()
                0 -> {
                    val user = binding.userIn.text.toString()
                    val pass = binding.pass1in.text.toString()
                    users.add(User(user,pass))
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        }
    }
}