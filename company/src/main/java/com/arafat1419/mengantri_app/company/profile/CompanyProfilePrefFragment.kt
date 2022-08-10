package com.arafat1419.mengantri_app.company.profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.vo.Resource
import com.arafat1419.mengantri_app.databinding.BottomConfirmationBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyProfilePrefFragment : PreferenceFragmentCompat() {

    private val navHostFragment: Fragment? by lazy {
        parentFragment?.parentFragmentManager?.findFragmentById(R.id.fragment_container)
    }

    private val sessionManager: CompanySessionManager by lazy {
        CompanySessionManager(requireContext())
    }

    private val customerSessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(requireContext())
    }

    private val viewModel: CompanyProfileViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.company_profile_pref, rootKey)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(com.arafat1419.mengantri_app.assets.R.string.key_edit_profile) -> {
                navHostFragment?.findNavController()?.navigate(
                    R.id.action_companyProfileFragment_to_companyEditProfileFragment
                )
            }
            getString(com.arafat1419.mengantri_app.assets.R.string.key_sign_out) -> {
                showBottomMessage()
            }
        }
        return true
    }

    private fun showBottomMessage() {
        val sheetBinding = BottomConfirmationBinding.inflate(LayoutInflater.from(context))
        val builder = BottomSheetDialog(requireContext())

        sheetBinding.apply {
            txtMessageTitle.text =
                getString(com.arafat1419.mengantri_app.assets.R.string.sign_out_title)
            txtMessage.text =
                getString(com.arafat1419.mengantri_app.assets.R.string.sign_out_message)

            btnYes.setOnClickListener {
                navigateToLogin()
                builder.dismiss()
            }

            btnNo.setOnClickListener {
                builder.dismiss()
            }
        }

        builder.apply {
            setCancelable(true)
            setContentView(sheetBinding.root)
            show()
        }
    }

    private fun unSubscribeTopic(
        customerMessageToken: String,
        onDeleteSuccess: (Boolean) -> Unit
    ) {
        viewModel.unSubscribeTopic(customerMessageToken).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val isSuccess = result.data

                    if (isSuccess == true) {
                        onDeleteSuccess(isSuccess)
                    }
                }
            }

        }
    }

    private fun navigateToLogin() {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.login.ui.LoginActivity")
            ).also {
                unSubscribeTopic(
                    getString(com.arafat1419.mengantri_app.R.string.app_name) + customerSessionManager.fetchCustomerId()
                ) { isSuccess ->
                    if (isSuccess) {
                        sessionManager.clearCompany()
                        customerSessionManager.clearCustomer()
                        startActivity(it)
                        activity?.finish()
                    } else {
                        Toast.makeText(
                            context,
                            getString(com.arafat1419.mengantri_app.assets.R.string.logout_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                com.arafat1419.mengantri_app.assets.R.string.module_not_found,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}