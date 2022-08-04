package com.arafat1419.mengantri_app.company.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val binding get() = _binding!!

    private val sessionManager: CompanySessionManager by lazy {
        CompanySessionManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, CompanyProfilePrefFragment())
            .commit()

        binding.apply {
            Glide.with(requireContext())
                .load(DataMapper.imageDirectus + sessionManager.fetchCompanyImage())
                .into(imgImage)

            txtName.text = sessionManager.fetchCompanyName()
            txtId.text = getString(
                com.arafat1419.mengantri_app.R.string.id_format,
                sessionManager.fetchCompanyId().toString()
            )
        }
    }
}