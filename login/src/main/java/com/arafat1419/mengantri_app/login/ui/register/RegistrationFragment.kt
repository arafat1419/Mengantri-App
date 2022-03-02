package com.arafat1419.mengantri_app.login.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.login.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = parentFragmentManager.findFragmentById(R.id.fragment_container)

        binding?.apply {
            btnRegistrationSignin.setOnClickListener {
                navHostFragment?.findNavController()
                    ?.navigate(R.id.action_registrationFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}