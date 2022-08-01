package com.arafat1419.mengantri_app.profile.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class ProfilePrefFragment : PreferenceFragmentCompat() {

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

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.key_edit_profile) -> {

            }
            getString(R.string.key_change_password) -> {

            }
            getString(R.string.key_my_favorite) -> {

            }
        }
        return true
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
}