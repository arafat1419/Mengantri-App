package com.arafat1419.mengantri_app.company.home

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
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyHomeBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.ui.adapter.ServicesAdapter
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import com.arafat1419.mengantri_app.core.vo.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyHomeFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyHomeBinding? = null
    private val binding get() = _binding!!

    private val navHostFragment: Fragment? by lazy { parentFragmentManager.findFragmentById(R.id.fragment_container) }

    // Initialize viewModel with koin
    private val viewModel: CompanyHomeViewModel by viewModel()

    private val servicesAdapter by lazy { ServicesAdapter() }

    private val companySessionManager: CompanySessionManager by lazy {
        CompanySessionManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        checkIntentFromOtherModule()
        setRecyclerView()
        getCompanyService()
        onItemClicked()
    }

    private fun onItemClicked() {
        servicesAdapter.onItemClicked = {
            val bundle = bundleOf(
                CompanyCustomersFragment.EXTRA_SERVICE_ID to it.service?.serviceId,
                CompanyCustomersFragment.EXTRA_SERVICE_NAME to it.service?.serviceName
            )
            navHostFragment?.findNavController()?.navigate(
                R.id.action_companyHomeFragment_to_companyCustomersFragment,
                bundle
            )
        }
    }

    private fun checkIntentFromOtherModule() {
        val isFromOtherModule = activity?.intent?.getBooleanExtra(StatusHelper.EXTRA_FRAGMENT_STATUS, false)

        if (isFromOtherModule == true) {
            binding.apply {
                val bottomNavigationView =
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.visibility = View.GONE

                navHostFragment?.findNavController()?.navigate(
                    R.id.action_companyHomeFragment_to_companyEditProfileFragment
                )
            }
        }
    }

    private fun getCompanyService() {
        viewModel.getServicesCount(companySessionManager.fetchCompanyId())
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
                        val listServiceCount = result.data

                        if (listServiceCount.isNullOrEmpty()) {
                            isEmpty(true)
                        } else {
                            isEmpty(false)

                            servicesAdapter.setData(listServiceCount)
                            servicesAdapter.notifyDataSetChanged()
                        }
                    }
                }
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
                rvCompanyServices.visibility = View.GONE

                empty.root.visibility = View.VISIBLE
                empty.btnAction.visibility = View.GONE
            } else {
                empty.root.visibility = View.GONE
                rvCompanyServices.visibility = View.VISIBLE
            }
        }
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding.rvCompanyServices.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = servicesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}