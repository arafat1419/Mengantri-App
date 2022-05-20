package com.arafat1419.mengantri_app.company.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyServiceBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.company.services.detailservices.CompanyDetailServiceFragment
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.CompanyServicesAdapter
import com.arafat1419.mengantri_app.core.ui.adapter.ServicesAdapter
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyServiceFragment : Fragment(), AdapterCallback<ServiceDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyServiceBinding? = null
    private val binding get() = _binding

    private lateinit var companySessionManager: CompanySessionManager

    private var navHostFragment: Fragment? = null

    // Initialize viewModel with koin
    private val viewModel: CompanyServicesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyServiceBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        setRecyclerView()

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(R.id.company_fragment_container)

        companySessionManager = CompanySessionManager(requireContext())

        viewModel.getServices(companySessionManager.fetchCompanyId()).observe(viewLifecycleOwner) {
            binding?.rvEditServices?.adapter.let { adapter ->
                when (adapter) {
                    is CompanyServicesAdapter -> {
                        adapter.setData(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        binding?.btnAddService?.setOnClickListener {
            navHostFragment?.findNavController()?.navigate(
                R.id.action_companyServiceFragment_to_companyDetailServiceFragment
            )
        }
    }

    override fun onItemClicked(data: ServiceDomain) {
        val bundle = bundleOf(
            CompanyDetailServiceFragment.EXTRA_SERVICE_DOMAIN to data
        )
        navHostFragment?.findNavController()?.navigate(
            R.id.action_companyServiceFragment_to_companyDetailServiceFragment,
            bundle
        )
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding?.rvEditServices?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = CompanyServicesAdapter(this@CompanyServiceFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}