package com.arafat1419.mengantri_app.login.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.login.R
import com.arafat1419.mengantri_app.login.databinding.FragmentBiodataBinding
import com.arafat1419.mengantri_app.login.di.loginModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@FlowPreview
@ExperimentalCoroutinesApi
class BiodataFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentBiodataBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: RegistrationViewModel by viewModel()

    // Initialize sessionManager to store data after fill biodata in CustomerSessionManager
    private lateinit var sessionManager: CustomerSessionManager

    private var navHostFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBiodataBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get customer id and customer email from Registration Verification Fragment
        val customerId = arguments?.getInt(EXTRA_CUSTOMER_ID)
        val customerEmail = arguments?.getString(EXTRA_CUSTOMER_EMAIL)

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        // Initialize session manager from customer session manager
        sessionManager = CustomerSessionManager(requireContext())

        // Initialize nav host fragment as fragment container
        navHostFragment = parentFragmentManager.findFragmentById(R.id.login_container)


        binding?.apply {
            // Set edtEmail from customer email
            edtBiodataEmail.setText(customerEmail)

            // Will navigate to login
            btnBiodataSignin.setOnClickListener {
                navigateToLogin()
            }

            // check if every edit text has fill
            // and update biodata customer
            // email from biodata will be check if email from response equal email from biodata
            // if true then response will be save to sessionManager and navigate to login
            btnBiodataSignup.setOnClickListener {
                if (checkEditText()) {
                    viewModel.updateBiodata(
                        customerId!!,
                        edtBiodataName.text.toString(),
                        edtBiodataPassword.text.toString(),
                        edtBiodataPhone.text.toString(),
                        ""
                    ).observe(viewLifecycleOwner) { customerDomain ->
                        if (customerDomain.customerEmail == edtBiodataEmail.text.toString()) {
                            Toast.makeText(context, R.string.registration_completed, Toast.LENGTH_SHORT)
                                .show()
                            sessionManager.clearCustomer()
                            sessionManager.saveCustomer(customerDomain)
                            navigateToLogin()
                        } else {
                            Toast.makeText(context, R.string.registration_failed, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        // Navigate to Login
        navHostFragment?.findNavController()
            ?.navigate(R.id.action_biodataFragment_to_loginFragment)
    }

    private fun checkEditText(): Boolean {
        var check = false
        binding?.apply {
            check = when {
                edtBiodataPassword.text?.isEmpty() == true -> {
                    Toast.makeText(context, com.arafat1419.mengantri_app.assets.R.string.password_cannot_empty, Toast.LENGTH_SHORT)
                        .show()
                    false
                }
                edtBiodataName.text?.isEmpty() == true -> {
                    Toast.makeText(context, com.arafat1419.mengantri_app.assets.R.string.name_cannot_empty, Toast.LENGTH_SHORT)
                        .show()
                    false
                }
                edtBiodataPhone.text?.isEmpty() == true -> {
                    Toast.makeText(context, com.arafat1419.mengantri_app.assets.R.string.phone_cannot_empty, Toast.LENGTH_SHORT)
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

    companion object {
        const val EXTRA_CUSTOMER_EMAIL = "extra_customer_email"
        const val EXTRA_CUSTOMER_ID = "extra_customer_id"
    }
}