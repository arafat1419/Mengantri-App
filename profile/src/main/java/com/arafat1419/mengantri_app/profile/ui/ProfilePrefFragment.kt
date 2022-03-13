package com.arafat1419.mengantri_app.profile.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.profile.R
import com.arafat1419.mengantri_app.profile.databinding.ModalEditProfileBinding
import com.arafat1419.mengantri_app.profile.di.profileModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class ProfilePrefFragment : PreferenceFragmentCompat() {
    // Initialize viewModel with koin
    private val viewModel: ProfileViewModel by viewModel()

    private lateinit var sessionManager: CustomerSessionManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.profile_preferences, rootKey)

        // Load koin manually for multi modules
        loadKoinModules(profileModule)

        // Initialize session manager from customer session manager
        sessionManager = CustomerSessionManager(requireContext())

        val editProfilePref = findPreference<Preference>(getString(R.string.key_edit_profile))
        editProfilePref?.setOnPreferenceClickListener {
            showEditProfileModal()
            true
        }
    }

    private fun showEditProfileModal() {
        val modalBinding: ModalEditProfileBinding =
            ModalEditProfileBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(requireActivity())

        val metrics = requireActivity().resources.displayMetrics
        val width = (6 * metrics.widthPixels) / 7

        modalBinding.apply {
            edtModalEProfleName.width = width
            edtModalEProfilePhone.width = width
            btnModalEProfile.width = width

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(modalBinding.root)
            dialog.show()

            btnModalEProfileClose.setOnClickListener {
                dialog.dismiss()
            }

            btnModalEProfile.setOnClickListener {
                editProfileSaveDialog()
            }

        }
    }

    private fun editProfileSaveDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Edit Profile")
        builder.setMessage(R.string.edit_profile_message)
            .setPositiveButton(com.arafat1419.mengantri_app.assets.R.string.yes) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(com.arafat1419.mengantri_app.assets.R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        builder.create()
        builder.show()
    }
}