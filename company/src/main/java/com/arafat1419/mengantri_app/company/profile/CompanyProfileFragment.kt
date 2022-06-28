package com.arafat1419.mengantri_app.company.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyProfileBinding
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.utils.DataMapper
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class CompanyProfileFragment : Fragment() {
    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyProfileBinding? = null
    private val binding get() = _binding

    private lateinit var sessionManager: CompanySessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize session manager from customer session manager
        sessionManager = CompanySessionManager(requireContext())

        if (view.findViewById<FrameLayout>(R.id.frame_c_profile) != null) {
            // below line is to inflate our fragment.
            parentFragmentManager.beginTransaction()
                .add(R.id.frame_c_profile, CompanyProfilePrefFragment())
                .commit()
        }

        binding?.apply {
            Glide.with(requireContext())
                .load(DataMapper.imageDirectus + sessionManager.fetchCompanyImage())
                .into(imgCProfile)

            txtCProfileName.text = sessionManager.fetchCompanyName()
            txtCProfileId.text = getString(
                com.arafat1419.mengantri_app.R.string.id_format,
                sessionManager.fetchCompanyId().toString()
            )
            txtProfileVersion.text = "1.1.0"

            btnProfileLogOut.setOnClickListener {
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.ui.MainActivity")
            ).also {
                startActivity(it)
                activity?.finish()
            }
        } catch (e: Exception) {
            Toast.makeText(context, com.arafat1419.mengantri_app.assets.R.string.module_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}