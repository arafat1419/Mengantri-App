package com.arafat1419.mengantri_app.company.profile

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyProfilePrefFragment : PreferenceFragmentCompat() {
    // Initialize viewModel with koin
    private val viewModel: CompanyProfileViewModel by viewModel()

    private lateinit var sessionManager: CompanySessionManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.company_profile_pref, rootKey)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        // Initialize session manager from customer session manager
        sessionManager = CompanySessionManager(requireContext())
    }
}