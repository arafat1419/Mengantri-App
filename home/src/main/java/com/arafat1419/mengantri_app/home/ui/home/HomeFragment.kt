package com.arafat1419.mengantri_app.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.arafat1419.mengantri_app.home.databinding.FragmentHomeBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.companies.CompaniesFragment
import com.arafat1419.mengantri_app.home.ui.detail.detailticket.DetailTicketFragment
import com.arafat1419.mengantri_app.home.ui.services.ServicesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.dmoral.toasty.Toasty
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

    private var navHostFragment: Fragment? = null

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment = parentFragmentManager.findFragmentById(R.id.fragment_container)

        checkIntentFromOtherModule()
        getCategories()
        getNewestCompanies()
        checkCustomerCompany()
        onItemClicked()

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
        viewModel.getCategories().observe(viewLifecycleOwner) { listCategories ->
            if (!listCategories.isNullOrEmpty()) {
                categoriesAdapter.setData(listCategories)
                categoriesAdapter.notifyDataSetChanged()
            }
        }
    }

    // get newest companies from view model in set the data to newest adapter
    private fun getNewestCompanies() {
        viewModel.getNewestComapanies().observe(viewLifecycleOwner) { listNewestCompanies ->
            if (!listNewestCompanies.isNullOrEmpty()) {
                newestAdapter.setData(listNewestCompanies)
                newestAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun checkCustomerCompany() {
        val customerId = customerSessionManager.fetchCustomerId()
        viewModel.getCustomerCompany(customerId).observe(viewLifecycleOwner) { listCompany ->
            binding.apply {
                if (listCompany.isNullOrEmpty()) {
                    // TODO : Add intent to companyRegistration
                } else {
                    when (listCompany[0].companyStatus) {
                        0 -> Toasty.error(
                            requireContext(),
                            getString(com.arafat1419.mengantri_app.assets.R.string.registration_on_process)
                        )
                        2 -> Toasty.error(
                            requireContext(),
                            getString(com.arafat1419.mengantri_app.assets.R.string.registration_expired)
                        )
                        else -> cardJoin.visibility = View.GONE
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

        binding.edtHomeSearch.setOnClickListener {
            navHostFragment?.findNavController()?.navigate(
                R.id.action_homeFragment_to_searchFragment
            )
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