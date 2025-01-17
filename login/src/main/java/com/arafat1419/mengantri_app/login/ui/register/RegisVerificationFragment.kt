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
import com.arafat1419.mengantri_app.login.databinding.FragmentRegisVerificationBinding
import com.arafat1419.mengantri_app.login.di.loginModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@FlowPreview
@ExperimentalCoroutinesApi
class RegisVerificationFragment : Fragment() {
    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentRegisVerificationBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: RegistrationViewModel by viewModel()

    // Initialize navHostFragment as fragment
    private var navHostFragment: Fragment? = null

    // Initialize customerCode and customerId as global variable
    private var customerCode: String? = null
    private val customerId by lazy { arguments?.getInt(EXTRA_CUSTOMER_ID) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisVerificationBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get customer email from RegistrationFragmen
        val customerEmail = arguments?.getString(EXTRA_CUSTOMER_EMAIL)

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        // Initialize nav host fragment as fragment container
        navHostFragment = parentFragmentManager.findFragmentById(R.id.login_container)

        // This function to set customerId and customerCode from customer domain in getLogin
        getCustomerDomain(customerEmail, false)

        binding?.apply {
            btnVerifResend.setOnClickListener {
                resendCode()
            }
            btnVerifSignup.setOnClickListener {
                if (checkEditText()) {
                    // This function to set customerId and customerCode from customer domain in getLogin
                    // and check if customerCode from customer domain equal edtVerifcode
                    // and customerEmail and customerId from customerDomain will be send to BiodataFragment
                    getCustomerDomain(customerEmail, true)
                }
            }
        }
    }

    private fun getCustomerDomain(customerEmail: String?, click: Boolean) {
        if (customerEmail != null) {
            viewModel.getLogin(customerEmail).observe(viewLifecycleOwner) { listCustomerDomain ->
                if (listCustomerDomain.isNotEmpty()) {
                    customerCode = listCustomerDomain[0].customerCode

                    if (click) {
                        if (customerCode == binding?.edtVerifCode?.text.toString()) {
                            val bundle = bundleOf(
                                BiodataFragment.EXTRA_CUSTOMER_EMAIL to customerEmail,
                                BiodataFragment.EXTRA_CUSTOMER_ID to customerId
                            )
                            navHostFragment?.findNavController()?.navigate(
                                R.id.action_regisVerificationFragment_to_biodataFragment, bundle
                            )
                        } else {
                            Toast.makeText(
                                context,
                                R.string.code_expired_please_resend,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    // This function use for resend new code by update customer code
    // Customer code length < 6 because server will send verification code if customer code length < 6
    private fun resendCode() {
        if (customerId != null) {
            val newCustomerCode = (10000..99999).random().toString()
            viewModel.updateCustomerCode(customerId!!, newCustomerCode)
                .observe(viewLifecycleOwner) {
                    if (it != null) {
                        Toast.makeText(
                            context,
                            R.string.check_email_for_new_verification,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun checkEditText(): Boolean {
        var check = false
        binding?.apply {
            check = if (edtVerifCode.text?.isEmpty() == true) {
                Toast.makeText(context, com.arafat1419.mengantri_app.assets.R.string.code_cannot_empty, Toast.LENGTH_SHORT).show()
                false
            } else true
        }
        return check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_CUSTOMER_EMAIL = "extra_customer_email"
        const val EXTRA_CUSTOMER_ID = "extra_customer_id"
    }
}