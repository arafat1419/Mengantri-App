package com.arafat1419.mengantri_app.login.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.assets.R
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
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: RegistrationViewModel by viewModel()

    // Initialize navHostFragment as fragment
    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.login.R.id.login_container)
    }

    private var customerId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        onItemClicked()
    }

    private fun onItemClicked() {
        binding.apply {
            btnLogin.setOnClickListener {
                // Navigate to loginFragment using navigation
                navigateToLogin()
            }

            btnSend.setOnClickListener {
                if (edtEmail.text.isNullOrEmpty()) {
                    Toast.makeText(context, R.string.email_cannot_empty, Toast.LENGTH_SHORT).show()
                } else {
                    if (customerId == null) {
                        postRegistration(edtEmail.text.toString().trim())
                    } else {
                        resendCode()
                    }
                }
            }

            btnVerification.setOnClickListener {
                // Check if all field has been filled
                if (edtCustomerCode.text.isNullOrEmpty()) {
                    Toast.makeText(context, R.string.code_cannot_empty, Toast.LENGTH_SHORT).show()
                } else {
                    if (customerId != null) {
                        checkCustomerCode(edtCustomerCode.text.toString().trim())
                    }
                }
            }
        }
    }

    private fun postRegistration(customerEmail: String) {
        viewModel.postRegistration(customerEmail).observe(viewLifecycleOwner) { customer ->
            if (customer != null) {
                Toast.makeText(context, R.string.send_email_verification, Toast.LENGTH_SHORT)
                    .show()
                binding.apply {
                    edtEmail.isEnabled = false
                    btnVerification.isEnabled = true
                    edtCustomerCode.isEnabled = true
                }
                customerId = customer.customerId
            }
        }
    }

    // This function use for resend new code by update customer code
    // Customer code length < 6 because server will send verification code if customer code length < 6
    private fun resendCode() {
        val newCustomerCode = (10000..99999).random().toString()
        viewModel.updateCustomerCode(customerId!!, newCustomerCode).observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(
                    context,
                    R.string.resend_email_verification,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkCustomerCode(customerCode: String) {
        viewModel.getCustomer(customerId!!).observe(viewLifecycleOwner) { customer ->
            if (customer != null) {
                if (customer.customerCode == customerCode) {
                    navigateToBiodata(customer.customerEmail)
                } else {
                    Toast.makeText(
                        context,
                        R.string.wrong_verification_code,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navigateToLogin() {
        // Navigate to Login
        navHostFragment?.findNavController()?.navigate(
            com.arafat1419.mengantri_app.login.R.id.action_registrationFragment_to_loginFragment
        )
    }

    private fun navigateToBiodata(customerEmail: String?) {
        val bundle = bundleOf(
            BiodataFragment.EXTRA_CUSTOMER_EMAIL to customerEmail,
            BiodataFragment.EXTRA_CUSTOMER_ID to customerId
        )
        navHostFragment?.findNavController()?.navigate(
            com.arafat1419.mengantri_app.login.R.id.action_registrationFragment_to_biodataFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}