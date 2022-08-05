package com.arafat1419.mengantri_app.login.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.model.CustomerDomain
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
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
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: LoginViewModel by viewModel()

    private val sessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(requireContext())
    }

    private val companySessionManager: CompanySessionManager by lazy {
        CompanySessionManager(requireContext())
    }

    // Initialize navHostFragment as fragment
    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.login.R.id.login_container)
    }

    private val roleArray: ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        setRole()
        onItemClicked()
    }

    private fun setRole() {
        binding.apply {

            roleArray.add(getString(R.string.customer))
            roleArray.add(getString(R.string.company))

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                roleArray
            )

            spnLoginAs.apply {
                setAdapter(adapter)
                adapter.setNotifyOnChange(true)
            }
        }
    }

    private fun onItemClicked() {
        binding.apply {
            btnRegister.setOnClickListener {
                navigateToRegister()
            }

            btnLogin.setOnClickListener {
                if (checkEditText()) {
                    loginCustomer(
                        edtEmail.text.toString().trim(),
                        edtPassword.text.toString().trim()
                    ) { customer ->
                        sessionManager.clearCustomer()
                        sessionManager.saveCustomer(customer)

                        if (spnLoginAs.text.toString().trim() == roleArray[0]) {
                            navigateToHome(customer.customerEmail)
                        } else {
                            loginCompany(customer.customerId!!) {
                                companySessionManager.clearCompany()
                                companySessionManager.saveCompany(it)

                                navigateToCompany()
                            }
                        }
                    }

                }
            }
        }
    }

    private fun loginCustomer(
        email: String,
        password: String,
        onLoginSuccess: (CustomerDomain) -> Unit
    ) {
        viewModel.getLogin(email).observe(viewLifecycleOwner) { listCustomer ->
            if (listCustomer != null) {
                listCustomer[0].customerPassword?.let {
                    viewModel.checkHash(password, it).observe(viewLifecycleOwner) { hashStatus ->
                        if (hashStatus) {
                            onLoginSuccess(listCustomer[0])
                        } else {
                            Toast.makeText(context, R.string.wrong_password, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun loginCompany(customerId: Int, onLoginSuccess: (CompanyDomain) -> Unit) {
        viewModel.getCustomerCompany(customerId).observe(viewLifecycleOwner) { listCompany ->
            if (listCompany.isNullOrEmpty()) {
                Toast.makeText(
                    context, getString(R.string.registration_not_found), Toast.LENGTH_SHORT
                ).show()
            } else {
                when (listCompany[0].companyStatus) {
                    0 -> Toast.makeText(
                        context, getString(R.string.registration_on_process), Toast.LENGTH_SHORT
                    ).show()
                    1 -> {
                        onLoginSuccess(listCompany[0])
                    }
                    2 -> Toast.makeText(
                        context, getString(R.string.registration_expired), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // Customer will be directly go to home when their data has been store in CustomerSessionManager
    override fun onStart() {
        super.onStart()
        val customerEmail = sessionManager.fetchCustomerEmail()
        val companyId = companySessionManager.fetchCompanyId()

        if (companyId != -1) {
            navigateToCompany()
        } else {
            navigateToHome(customerEmail)
        }
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
                Toast.makeText(
                    context,
                    R.string.module_not_found,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToCompany() {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.company.CompanyActivity")
            ).also {
                startActivity(it)
                activity?.finish()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                R.string.module_not_found,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToRegister() {
        // Navigate to registrationFragment using navigation
        navHostFragment?.findNavController()?.navigate(
            com.arafat1419.mengantri_app.login.R.id.action_loginFragment_to_registrationFragment
        )
    }

    private fun checkEditText(): Boolean {
        var check: Boolean
        binding.apply {
            check = when {
                spnLoginAs.text?.isEmpty() == true -> {
                    Toast.makeText(context, R.string.email_cannot_empty, Toast.LENGTH_SHORT).show()
                    false
                }
                edtEmail.text?.isEmpty() == true -> {
                    Toast.makeText(context, R.string.email_cannot_empty, Toast.LENGTH_SHORT).show()
                    false
                }
                edtPassword.text?.isEmpty() == true -> {
                    Toast.makeText(context, R.string.password_cannot_empty, Toast.LENGTH_SHORT)
                        .show()
                    false
                }
                edtPassword.text?.toString()?.length!! < 8 -> {
                    Toast.makeText(
                        context,
                        getString(R.string.minimum_password),
                        Toast.LENGTH_SHORT
                    ).show()
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