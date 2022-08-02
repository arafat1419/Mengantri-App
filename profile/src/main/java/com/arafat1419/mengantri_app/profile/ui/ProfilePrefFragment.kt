package com.arafat1419.mengantri_app.profile.ui

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
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.databinding.BottomConfirmationBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class ProfilePrefFragment : PreferenceFragmentCompat() {

    private val navHostFragment: Fragment? by lazy {
        parentFragment?.parentFragmentManager?.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)
    }

    private val sessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(
            requireContext()
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(
            com.arafat1419.mengantri_app.profile.R.xml.profile_preferences,
            rootKey
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.key_edit_profile) -> {
                navHostFragment?.findNavController()?.navigate(
                    com.arafat1419.mengantri_app.R.id.action_profileFragment_to_changeProfileFragment
                )
            }
            getString(R.string.key_change_password) -> {
                navHostFragment?.findNavController()?.navigate(
                    com.arafat1419.mengantri_app.R.id.action_profileFragment_to_changePasswordFragment
                )
            }
            getString(R.string.key_my_favorite) -> {
                navigateToCompany()
            }
            getString(R.string.key_sign_out) -> {
                showBottomMessage()
            }
        }
        return true
    }

    private fun showBottomMessage() {
        val sheetBinding = BottomConfirmationBinding.inflate(LayoutInflater.from(context))
        val builder = BottomSheetDialog(requireContext())

        sheetBinding.apply {
            txtMessageTitle.text = getString(R.string.sign_out_title)
            txtMessage.text = getString(R.string.sign_out_message)

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

    private fun navigateToLogin() {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.login.ui.LoginActivity")
            ).also {
                startActivity(it)
                activity?.finish()
                sessionManager.clearCustomer()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                R.string.module_not_found,
                Toast.LENGTH_SHORT
            ).show()
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