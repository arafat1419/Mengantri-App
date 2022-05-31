package com.arafat1419.mengantri_app.profile.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.profile.R
import com.arafat1419.mengantri_app.profile.databinding.ModalEditPasswordBinding
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

        val changePassPref = findPreference<Preference>(getString(R.string.key_change_password))
        changePassPref?.setOnPreferenceClickListener {
            showChangePassModal()
            true
        }

        val joinUsPref = findPreference<Preference>(getString(R.string.key_join_us))
        joinUsPref?.setOnPreferenceClickListener {
            navigateToCompany()
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
            edtModalEProfileName.width = width
            edtModalEProfilePhone.width = width
            btnModalEProfile.width = width

            edtModalEProfileName.setText(sessionManager.fetchCustomerName())
            edtModalEProfilePhone.setText(sessionManager.fetchCustomerPhone())

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(modalBinding.root)
            dialog.show()

            btnModalEProfileClose.setOnClickListener {
                dialog.dismiss()
            }

            btnModalEProfile.setOnClickListener {
                if (edtModalEProfileName.text.toString()
                        .isEmpty() || edtModalEProfilePhone.text.toString().isEmpty()
                ) {
                    Toast.makeText(context, "Fill cannot empty", Toast.LENGTH_SHORT).show()
                } else {
                    editProfileSaveDialog(
                        sessionManager.fetchCustomerId(),
                        edtModalEProfileName.text.toString(),
                        edtModalEProfilePhone.text.toString()
                    )
                    dialog.dismiss()
                }
            }

        }
    }

    private fun editProfileSaveDialog(customerId: Int, newName: String, newPhone: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Edit Profile")
        builder.setMessage(R.string.edit_profile_message)
            .setPositiveButton(com.arafat1419.mengantri_app.assets.R.string.yes) { dialog, _ ->
                viewModel.updateProfile(customerId, newName, newPhone)
                    .observe(viewLifecycleOwner) { customerDomain ->
                        if (customerDomain != null) {
                            sessionManager.clearCustomer()
                            sessionManager.saveCustomer(customerDomain)
                            Toast.makeText(context, "Profile has been updated", Toast.LENGTH_SHORT)
                                .show()
                        }
                        dialog.cancel()
                    }
            }
            .setNegativeButton(com.arafat1419.mengantri_app.assets.R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        builder.create()
        builder.show()
    }

    private fun showChangePassModal() {
        val modalBinding: ModalEditPasswordBinding =
            ModalEditPasswordBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(requireActivity())

        val metrics = requireActivity().resources.displayMetrics
        val width = (6 * metrics.widthPixels) / 7

        modalBinding.apply {
            edtModalCPassCurrent.width = width
            edtModalCPassNew.width = width
            edtModalCPassConfirm.width = width
            btnModalCPass.width = width

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(modalBinding.root)
            dialog.show()

            btnModalCPassClose.setOnClickListener {
                dialog.dismiss()
            }

            btnModalCPass.setOnClickListener {
                if (edtModalCPassCurrent.text.toString()
                        .isNotEmpty() || edtModalCPassNew.text.toString().isNotEmpty()
                ) {
                    if (edtModalCPassCurrent.text.toString() == sessionManager.fetchCustomerPass()) {
                        if (edtModalCPassNew.text.toString() == edtModalCPassConfirm.text.toString()) {
                            editChangePassDialog(
                                sessionManager.fetchCustomerId(),
                                edtModalCPassConfirm.text.toString()
                            )
                            dialog.dismiss()
                        } else {
                            Toast.makeText(
                                context,
                                "New password and confirm password not same",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Current password wrong, try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "Fill cannot empty", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun editChangePassDialog(customerId: Int, newPassword: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Change Password")
        builder.setMessage(R.string.change_pass_message)
            .setPositiveButton(com.arafat1419.mengantri_app.assets.R.string.yes) { dialog, _ ->
                viewModel.updatePassword(customerId, newPassword)
                    .observe(viewLifecycleOwner) { customerDomain ->
                        if (customerDomain != null) {
                            sessionManager.clearCustomer()
                            sessionManager.saveCustomer(customerDomain)
                            Toast.makeText(context, "Password has been change", Toast.LENGTH_SHORT)
                                .show()
                        }
                        dialog.cancel()
                    }
            }
            .setNegativeButton(com.arafat1419.mengantri_app.assets.R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        builder.create()
        builder.show()
    }

    private fun navigateToCompany() {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.company.register.RegistrationStatusActivity")
            ).also {
                startActivity(it)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Module not found", Toast.LENGTH_SHORT).show()
        }
    }
}