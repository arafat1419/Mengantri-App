package com.arafat1419.mengantri_app.login.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.login.R
import com.arafat1419.mengantri_app.login.databinding.FragmentLoginBinding
import com.arafat1419.mengantri_app.login.di.loginModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class LoginFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: LoginViewModel by viewModel()

    private lateinit var sessionManager: CustomerSessionManager

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

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        // Initialize session manager from customer session manager
        sessionManager = CustomerSessionManager(requireContext())

        // Initialize nav host fragment as fragment container
        val navHostFragment = parentFragmentManager.findFragmentById(R.id.login_container)

        // Binding apply to reduce redundant code
        binding?.apply {
            btnLoginSignup.setOnClickListener {
                // Navigate to registrationFragment using navigation
                navHostFragment?.findNavController()
                    ?.navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            btnLoginSignin.setOnClickListener {
                // Check if all email and password field has been filled
                if (checkEditText()) {
                    // Get data from view model with email as parameter
                    viewModel.getLogin(edtLoginEmail.text.toString())
                        .observe(viewLifecycleOwner) { customerDomain ->
                            if (customerDomain.isNotEmpty()) {
                                // Check if customer password is equal with password from field
                                customerDomain[0].customerPassword?.let { it1 ->
                                    viewModel.checkHash(edtLoginPassword.text.toString(), it1)
                                        .observe(viewLifecycleOwner) { hashStatus ->
                                            if (hashStatus) {
                                                // Save session customer domain to customer session manager
                                                sessionManager.saveCustomer(customerDomain[0])

                                                // Navigate to home
                                                navigateToHome(customerDomain[0].customerEmail)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    R.string.wrong_password,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            } else {
                                Toast.makeText(context, R.string.wrong_email, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    // Customer will be directly go to home when their data has been store in CustomerSessionManager
    override fun onStart() {
        super.onStart()
        // Fetch customer email from customer session manager and navigate when data is not null
        val customerEmail = sessionManager.fetchCustomerEmail()
        navigateToHome(customerEmail)
    }

    private fun navigateToHome(customerEmail: String?) {
        if (customerEmail != null) {
            // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
            try {
                Intent(
                    requireActivity(),
                    Class.forName("com.arafat1419.mengantri_app.ui.MainActivity")
                ).also {
                    startActivity(it)
                    activity?.finish()
                }
            } catch (e: Exception) {
                Toast.makeText(context, com.arafat1419.mengantri_app.assets.R.string.module_not_found, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkEditText(): Boolean {
        var check = false
        binding?.apply {
            check = when {
                edtLoginEmail.text?.isEmpty() == true -> {
                    Toast.makeText(context, com.arafat1419.mengantri_app.assets.R.string.email_cannot_empty, Toast.LENGTH_SHORT).show()
                    false
                }
                edtLoginPassword.text?.isEmpty() == true -> {
                    Toast.makeText(context, com.arafat1419.mengantri_app.assets.R.string.password_cannot_empty, Toast.LENGTH_SHORT)
                        .show()
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