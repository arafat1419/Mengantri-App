package com.arafat1419.mengantri_app.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.core.ui.adapter.CategoriesAdapter
import com.arafat1419.mengantri_app.core.ui.adapter.CompaniesAdapter
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_FRAGMENT_STATUS
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_TICKET_ID
import com.arafat1419.mengantri_app.core.vo.Resource
import com.arafat1419.mengantri_app.home.databinding.FragmentHomeBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.companies.CompaniesFragment
import com.arafat1419.mengantri_app.home.ui.detail.detailticket.DetailTicketFragment
import com.arafat1419.mengantri_app.home.ui.services.ServicesFragment
import com.arafat1419.mengantri_app.utils.LayoutHelper
import com.arafat1419.mengantri_app.utils.LayoutHelper.isLoading
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class HomeFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: HomeViewModel by viewModel()

    private val navHostFragment: Fragment? by lazy { parentFragmentManager.findFragmentById(R.id.fragment_container) }

    private val categoriesAdapter: CategoriesAdapter by lazy { CategoriesAdapter() }
    private val newestAdapter: CompaniesAdapter by lazy { CompaniesAdapter() }

    private val customerSessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
//        _loadingBinding = binding.loading
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        checkIntentFromOtherModule()
        getCategories()
        getNewestCompanies()

        onItemClicked()
        checkCustomerCompany()
    }

    private fun checkIntentFromOtherModule() {
        val isFromOtherModule = activity?.intent?.getBooleanExtra(EXTRA_FRAGMENT_STATUS, false)

        if (isFromOtherModule == true) {
            val ticketIdFromOtherModule = activity?.intent?.getIntExtra(EXTRA_TICKET_ID, -1)
            binding.apply {
                val bottomNavigationView =
                    requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation)
                bottomNavigationView.visibility = View.GONE

                val bundle = bundleOf(
                    DetailTicketFragment.EXTRA_TICKET_ID to ticketIdFromOtherModule,
                    DetailTicketFragment.EXTRA_FROM_OTHER to isFromOtherModule
                )
                navHostFragment?.findNavController()?.navigate(
                    R.id.action_homeFragment_to_detailTicketFragment,
                    bundle
                )
            }
        }
    }

    // get categories from view model in set the data to categories adapter
    private fun getCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    isLoading(binding.loading, false)
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> isLoading(binding.loading, true)
                is Resource.Success -> {
                    isLoading(binding.loading, false)
                    val listCategories = result.data

                    if (!listCategories.isNullOrEmpty()) {
                        categoriesAdapter.setData(listCategories)
                        categoriesAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    // get newest companies from view model in set the data to newest adapter
    private fun getNewestCompanies() {
        viewModel.getNewestComapanies().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    isLoading(binding.loading, false)
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> isLoading(binding.loading, true)
                is Resource.Success -> {
                    isLoading(binding.loading, false)
                    val listNewestCompanies = result.data

                    if (!listNewestCompanies.isNullOrEmpty()) {
                        newestAdapter.setData(listNewestCompanies)
                        newestAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun checkCustomerCompany() {
        val customerId = customerSessionManager.fetchCustomerId()
        viewModel.getCustomerCompany(customerId).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    isLoading(binding.loading, false)
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> isLoading(binding.loading, true)
                is Resource.Success -> {
                    binding.apply {
                        isLoading(binding.loading, false)
                        val listCompany = result.data

                        if (listCompany.isNullOrEmpty()) {
                            cardJoin.visibility = View.VISIBLE
                            cardJoin.setOnClickListener {
                                navigateToCompany()
                            }
                        } else {
                            if (listCompany[0].companyStatus != 1) {
                                cardJoin.visibility = View.VISIBLE
                                cardJoin.setOnClickListener {
                                    when (listCompany[0].companyStatus) {
                                        0 -> Toast.makeText(
                                            context,
                                            getString(com.arafat1419.mengantri_app.assets.R.string.registration_on_process),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        2 -> Toast.makeText(
                                            context,
                                            getString(com.arafat1419.mengantri_app.assets.R.string.registration_expired),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onItemClicked() {
        categoriesAdapter.onItemClicked = {
            val bundle = bundleOf(
                CompaniesFragment.EXTRA_COMPANY_ID to it.categoryId
            )
            navHostFragment?.findNavController()
                ?.navigate(R.id.action_homeFragment_to_companiesFragment, bundle)
        }

        newestAdapter.onItemClicked = {
            val bundle = bundleOf(
                ServicesFragment.EXTRA_COMPANY_DOMAIN to it
            )
            navHostFragment?.findNavController()?.navigate(
                R.id.action_homeFragment_to_servicesFragment, bundle
            )
        }

        binding.apply {
            edtHomeSearch.setOnClickListener {
                navHostFragment?.findNavController()?.navigate(
                    R.id.action_homeFragment_to_searchFragment
                )
            }
        }
    }

    private fun navigateToCompany() {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.company.CompanyActivity")
            ).also {
                it.putExtra(EXTRA_FRAGMENT_STATUS, true)
                startActivity(it)
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                com.arafat1419.mengantri_app.assets.R.string.module_not_found,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Set recycler view with grid and use categories adapter as adapter
    private fun setRecyclerView() {
        binding.apply {
            rvCategory.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = categoriesAdapter
            }
            rvNewest.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = newestAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}