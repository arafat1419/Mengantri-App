package com.arafat1419.mengantri_app.profile.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.profile.R
import com.arafat1419.mengantri_app.profile.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private lateinit var sessionManager: CustomerSessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize session manager from customer session manager
        sessionManager = CustomerSessionManager(requireContext())

        if (view.findViewById<FrameLayout>(R.id.frame_profile) != null) {
            // below line is to inflate our fragment.
            parentFragmentManager.beginTransaction()
                .add(R.id.frame_profile, ProfilePrefFragment())
                .commit()
        }

        binding?.apply {
            txtProfileName.text = sessionManager.fetchCustomerName()
            txtProfileEmail.text = sessionManager.fetchCustomerEmail()
            txtProfileVersion.text = "1.1.0"

            btnProfileLogOut.setOnClickListener {
                logOutDialog()
            }
        }
    }

    private fun logOutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Logout")
        builder.setMessage(R.string.logout_message)
            .setPositiveButton(com.arafat1419.mengantri_app.assets.R.string.yes) { _, _ ->
                navigateToLogin()
            }
            .setNegativeButton(com.arafat1419.mengantri_app.assets.R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        builder.create()
        builder.show()
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
            Toast.makeText(context, "Module not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}