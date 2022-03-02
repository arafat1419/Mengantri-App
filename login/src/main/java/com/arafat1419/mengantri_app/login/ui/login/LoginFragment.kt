package com.arafat1419.mengantri_app.login.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.login.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding?= null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = parentFragmentManager.findFragmentById(R.id.fragment_container)

        binding?.apply {
            btnLoginSignup.setOnClickListener {
                navHostFragment?.findNavController()?.navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            btnLoginSignin.setOnClickListener {
                navHostFragment?.findNavController()?.navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}