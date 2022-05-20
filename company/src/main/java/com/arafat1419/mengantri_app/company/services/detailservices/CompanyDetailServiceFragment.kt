package com.arafat1419.mengantri_app.company.services.detailservices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyDetailServiceBinding
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyServiceBinding

class CompanyDetailServiceFragment: Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyDetailServiceBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanyDetailServiceBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_SERVICE_ID = "extra_service_id"
    }
}