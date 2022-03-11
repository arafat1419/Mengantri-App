package com.arafat1419.mengantri_app.login.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.login.R
import com.arafat1419.mengantri_app.login.databinding.FragmentRegistrationBinding
import com.arafat1419.mengantri_app.login.di.loginModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@FlowPreview
@ExperimentalCoroutinesApi
class RegistrationFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: RegistrationViewModel by viewModel()

    // Initialize navHostFragment as fragment
    private var navHostFragment: Fragment? = null

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

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        // Set nav host fragment as fragment container from login activity
        navHostFragment = parentFragmentManager.findFragmentById(R.id.login_container)

        // binding apply to reduce redundant code
        binding?.apply {
            btnRegistrationSignin.setOnClickListener {
                // Navigate to loginFragment using navigation
                navigateToLogin()
            }

            btnRegistrationSignup.setOnClickListener {
                // Check if all field has been filled
                if (checkEditText()) {
                    // This viewModel just for post and don't need response
                    // So email still can receive verification code
                    // Navigate to verification with edtEmail as parameter
                    viewModel.postRegistration(edtRegisrationEmail.text.toString())
                        .observe(viewLifecycleOwner) {}
                    Toast.makeText(
                        context,
                        "Check your email for verification code",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToVerif(edtRegisrationEmail.text.toString())
                }
            }
        }
    }

    private fun navigateToLogin() {
        // Navigate to Login
        navHostFragment?.findNavController()
            ?.navigate(R.id.action_registrationFragment_to_loginFragment)
    }

    private fun navigateToVerif(customerEmail: String) {
        // Navigate to verif with customerEmail
        val bundle = bundleOf(
            RegisVerificationFragment.EXTRA_CUSTOMER_EMAIL to customerEmail
        )
        navHostFragment?.findNavController()?.navigate(
            R.id.action_registrationFragment_to_regisVerificationFragment, bundle
        )
    }

    private fun checkEditText(): Boolean {
        var check = false
        binding?.apply {
            check = when {
                edtRegisrationEmail.text?.isEmpty() == true -> {
                    Toast.makeText(context, "Email cannot empty", Toast.LENGTH_SHORT).show()
                    false
                }
                else -> {
                    true
                }
            }
        }
        return check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}