package com.arafat1419.mengantri_app.company.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arafat1419.mengantri_app.company.CompanyActivity
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.FragmentRegistrationStatusBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class RegistrationStatusFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentRegistrationStatusBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: RegisterStatusViewModel by viewModel()

    private lateinit var sessionManager: CustomerSessionManager


    // Initialize navHostFragment as fragment
    private var navHostFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationStatusBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        // Initialize session manager from customer session manager
        sessionManager = CustomerSessionManager(requireContext())

        // Initialize nav host fragment as fragment container
        navHostFragment = parentFragmentManager.findFragmentById(R.id.registration_container)

        viewModel.getUserCompany(sessionManager.fetchCustomerId())
            .observe(viewLifecycleOwner) { listCompany ->
                if (listCompany.isNullOrEmpty()) {
                    statusHandler(4)
                } else {
                    val status = listCompany[0].companyStatus!!
                    statusHandler(status)
                }
            }
    }

    private fun statusHandler(status: Int) {
        when (status) {
            0 -> {
                binding?.apply {
                    imgRStatus.setImageResource(com.arafat1419.mengantri_app.R.drawable.ic_progress_time)
                    txtRStatus.text =
                        getString(R.string.registration_on_process)
                    navigateToProfile()
                }
            }
            1 -> Intent(activity, CompanyActivity::class.java).also {
                startActivity(it)
                activity?.finish()
            }
            2 -> {
                binding?.apply {
                    imgRStatus.setImageResource(com.arafat1419.mengantri_app.R.drawable.ic_expired_time)
                    txtRStatus.text =
                        getString(R.string.registration_expired)
                    navigateToProfile()
                }
            }
            else -> {
                navHostFragment?.findNavController()?.navigate(R.id.action_registrationStatusFragment_to_companyEditProfileFragment)
            }
        }
    }

    private fun navigateToProfile() {
        binding?.btnRStatus?.setOnClickListener {
            activity?.onBackPressed()
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}