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
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyServiceBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.ui.adapter.CompanyServicesAdapter
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.vo.Resource
import com.arafat1419.mengantri_app.databinding.BottomConfirmationBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyServiceFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyServiceBinding? = null
    private val binding get() = _binding!!

    private val companySessionManager: CompanySessionManager by lazy {
        CompanySessionManager(
            requireContext()
        )
    }

    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(
            com.arafat1419.mengantri_app.company.R.id.fragment_container
        )
    }

    // Initialize viewModel with koin
    private val viewModel: CompanyServicesViewModel by viewModel()

    private val servicesAdapter: CompanyServicesAdapter by lazy { CompanyServicesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyServiceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        setRecyclerView()

        getServices()
        onItemClicked()
    }

    private fun getServices() {
        viewModel.getServices(companySessionManager.fetchCompanyId())
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading(false)
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        isLoading(true)
                    }
                    is Resource.Success -> {
                        isLoading(false)
                        val listService = result.data

                        if (listService.isNullOrEmpty()) {
                            isEmpty(true)
                        } else {
                            isEmpty(false)

                            servicesAdapter.setData(listService)
                            servicesAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }

    private fun deleteServices(serviceId: Int?) {
        if (serviceId != null) {
            viewModel.deleteService(serviceId).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading(false)
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        isLoading(true)
                    }
                    is Resource.Success -> {
                        isLoading(false)
                        val status = result.data

                        if (status == true) {
                            Toast.makeText(
                                context,
                                getString(R.string.delete_service_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            getServices()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.delete_service_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun onItemClicked() {
        servicesAdapter.onItemClicked = {
            val bundle = bundleOf(
                CompanyDetailServiceFragment.EXTRA_SERVICE_ID to it.serviceId
            )
            navHostFragment?.findNavController()?.navigate(
                com.arafat1419.mengantri_app.company.R.id.action_companyServiceFragment_to_companyDetailServiceFragment,
                bundle
            )
        }

        servicesAdapter.onDeleteClicked = {
            showDialogAlert(it)
        }

        binding.btnAddService.setOnClickListener {
            navHostFragment?.findNavController()?.navigate(
                com.arafat1419.mengantri_app.company.R.id.action_companyServiceFragment_to_companyDetailServiceFragment
            )
        }
    }

    private fun showDialogAlert(serviceDomain: ServiceDomain?) {
        val sheetBinding = BottomConfirmationBinding.inflate(LayoutInflater.from(context))
        val builder = BottomSheetDialog(requireContext())

        sheetBinding.apply {
            txtMessageTitle.text = getString(R.string.delete_service_title)
            txtMessage.text = getString(
                R.string.delete_service_message,
                serviceDomain?.serviceName
            )

            btnYes.setOnClickListener {
                deleteServices(serviceDomain?.serviceId)
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

    private fun isLoading(state: Boolean) {
        binding.loading.root.visibility = if (state) {
            isEmpty(false)
            View.VISIBLE
        } else View.GONE
    }

    private fun isEmpty(state: Boolean) {
        binding.apply {
            if (state) {
                rvEditServices.visibility = View.GONE

                empty.root.visibility = View.VISIBLE
                empty.btnAction.apply {
                    text = getString(R.string.add_service)
                    setOnClickListener {
                        navHostFragment?.findNavController()?.navigate(
                            com.arafat1419.mengantri_app.company.R.id.action_companyServiceFragment_to_companyDetailServiceFragment
                        )
                    }
                }
            } else {
                empty.root.visibility = View.GONE
                rvEditServices.visibility = View.VISIBLE
            }
        }
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding.rvEditServices.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = servicesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}