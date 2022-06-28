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
import com.arafat1419.mengantri_app.core.domain.model.CategoryDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.CategoriesAdapter
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_FRAGMENT_STATUS
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_TICKET_ID
import com.arafat1419.mengantri_app.home.databinding.FragmentHomeBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.companies.CompaniesFragment
import com.arafat1419.mengantri_app.home.ui.detail.detailticket.DetailTicketFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


@ExperimentalCoroutinesApi
@FlowPreview
class HomeFragment : Fragment(), AdapterCallback<CategoryDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: HomeViewModel by viewModel()

    private var navHostFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment = parentFragmentManager.findFragmentById(R.id.fragment_container)

        val isFromOtherModule = activity?.intent?.getBooleanExtra(EXTRA_FRAGMENT_STATUS, false)
        val ticketIdFromOtherModule = activity?.intent?.getIntExtra(EXTRA_TICKET_ID, -1)

        if (isFromOtherModule == true) {
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

        // get categories from view model in set the data to categories adapter
        viewModel.getCategories().observe(viewLifecycleOwner) { listCategories ->
            binding?.rvHomeCategory?.adapter.let { adapter ->
                when (adapter) {
                    is CategoriesAdapter -> {
                        adapter.setData(listCategories)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        binding?.edtHomeSearch?.setOnClickListener {
            navHostFragment?.findNavController()?.navigate(
                R.id.action_homeFragment_to_searchFragment
            )
        }
    }

    // move to companies fragment with category domain
    override fun onItemClicked(data: CategoryDomain) {
        val bundle = bundleOf(
            CompaniesFragment.EXTRA_COMPANY_ID to data.categoryId
        )
        navHostFragment?.findNavController()
            ?.navigate(R.id.action_homeFragment_to_companiesFragment, bundle)
    }

    // Set recycler view with grid and use categories adapter as adapter
    private fun setRecyclerView() {
        binding?.rvHomeCategory?.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = CategoriesAdapter(this@HomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}