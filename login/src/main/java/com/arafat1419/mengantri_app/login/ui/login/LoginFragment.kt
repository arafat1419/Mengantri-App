package com.arafat1419.mengantri_app.login.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.login.R
import com.arafat1419.mengantri_app.login.databinding.FragmentLoginBinding
import java.lang.Exception

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

        val navHostFragment = parentFragmentManager.findFragmentById(R.id.login_container)

        binding?.apply {
            btnLoginSignup.setOnClickListener {
                navHostFragment?.findNavController()?.navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            btnLoginSignin.setOnClickListener {
                try {
                    Intent(requireActivity(), Class.forName("com.arafat1419.mengantri_app.ui.MainActivity")).also {
                        startActivity(it)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Module not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}