package com.arafat1419.mengantri_app.login.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.vo.Resource
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
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: RegistrationViewModel by viewModel()

    // Initialize sessionManager to store data after fill biodata in CustomerSessionManager
    private val sessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(requireContext())
    }

    // Initialize navHostFragment as fragment
    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.login.R.id.login_container)
    }

    private val getCustomerId: Int? by lazy { arguments?.getInt(EXTRA_CUSTOMER_ID) }
    private val getCustomerEmail: String? by lazy { arguments?.getString(EXTRA_CUSTOMER_EMAIL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@BiodataFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBiodataBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        binding.edtEmail.setText(getCustomerEmail)

        onItemClicked()
    }

    private fun onItemClicked() {
        binding.apply {

            btnRegister.setOnClickListener {
                if (checkEditText() && getCustomerId != null) {
                    updateCustomer(
                        edtName.text.toString(),
                        edtPassword.text.toString().trim(),
                        edtPhone.text.toString().trim()
                    )
                }
            }
        }
    }

    private fun updateCustomer(name: String, password: String, phone: String) {
        viewModel.updateBiodata(getCustomerId!!, name, password, phone, "")
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading(false)
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> isLoading(true)
                    is Resource.Success -> {
                        isLoading(false)
                        val customer = result.data

                        if (customer != null) {
                            Toast.makeText(context, R.string.account_registered, Toast.LENGTH_SHORT)
                                .show()
                            sessionManager.clearCustomer()
                            sessionManager.saveCustomer(customer)
                            navigateToLogin()
                        }
                    }
                }
            }
    }

    private fun isLoading(state: Boolean) {
        binding.loading.root.visibility = if(state) View.VISIBLE else View.GONE
    }

    private fun navigateToLogin() {
        // Navigate to Login
        navHostFragment?.findNavController()?.navigate(
            com.arafat1419.mengantri_app.login.R.id.action_biodataFragment_to_loginFragment
        )
    }

    private fun checkEditText(): Boolean {
        var check: Boolean
        binding.apply {
            check = when {
                edtPassword.text?.isEmpty() == true -> {
                    Toast.makeText(
                        context,
                        R.string.password_cannot_empty,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    false
                }
                edtName.text?.isEmpty() == true -> {
                    Toast.makeText(
                        context,
                        R.string.name_cannot_empty,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    false
                }
                edtPhone.text?.isEmpty() == true -> {
                    Toast.makeText(
                        context,
                        R.string.phone_cannot_empty,
                        Toast.LENGTH_SHORT
                    )
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

    companion object {
        const val EXTRA_CUSTOMER_EMAIL = "extra_customer_email"
        const val EXTRA_CUSTOMER_ID = "extra_customer_id"
    }
}