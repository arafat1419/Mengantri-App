package com.arafat1419.mengantri_app.login.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arafat1419.mengantri_app.login.R
import com.arafat1419.mengantri_app.login.databinding.FragmentRegisVerificationBinding
import com.arafat1419.mengantri_app.login.databinding.FragmentRegistrationBinding
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

    private var navHostFragment: Fragment? = null

    private var customerEmail: String? = null

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

        customerEmail = arguments?.getString(EXTRA_CUSTOMER_EMAIL)

        // Load koin manually for multi modules
        loadKoinModules(loginModule)

        // Initialize nav host fragment as fragment container
        navHostFragment = parentFragmentManager.findFragmentById(R.id.login_container)

        binding?.apply {
            btnVerifSignup.setOnClickListener {
                if (checkEditText()) {
                }
            }
        }
    }

    private fun checkEditText(): Boolean {
        var check = false
        binding?.apply {
            check = if (edtVerifCode.text?.isEmpty() == true) {
                Toast.makeText(context, "Code cannot empty", Toast.LENGTH_SHORT).show()
                false
            } else edtVerifCode.text?.length!! >= 6
        }
        return check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_CUSTOMER_EMAIL = "extra_customer_email"
    }
}