package com.arafat1419.mengantri_app.home.ui.companies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.CompaniesAdapter
import com.arafat1419.mengantri_app.home.databinding.FragmentCompaniesBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.search.SearchFragment
import com.arafat1419.mengantri_app.home.ui.services.ServicesFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class CompaniesFragment : Fragment(), AdapterCallback<CompanyDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompaniesBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: CompaniesViewModel by viewModel()

    private var navHostFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@CompaniesFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompaniesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getCompanyId = arguments?.getInt(EXTRA_COMPANY_ID)

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(R.id.fragment_container)

        // get companies from view model in set the data to categories adapter
        if (getCompanyId != null) {
            viewModel.getCompanies(getCompanyId).observe(viewLifecycleOwner) { listCategories ->
                binding?.rvCompanies?.adapter.let { adapter ->
                    when (adapter) {
                        is CompaniesAdapter -> {
                            adapter.setData(listCategories)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        binding?.edtCompaniesSearch?.setOnClickListener {
            val bundle = bundleOf(
                SearchFragment.EXTRA_CATEGORY_ID to getCompanyId
            )
            navHostFragment?.findNavController()?.navigate(
                R.id.action_companiesFragment_to_searchFragment,
                bundle
            )
        }
    }

    // move to service fragment with company domain
    override fun onItemClicked(data: CompanyDomain) {
        val bundle = bundleOf(
            ServicesFragment.EXTRA_COMPANY_DOMAIN to data
        )
        navHostFragment?.findNavController()?.navigate(
            R.id.action_companiesFragment_to_servicesFragment, bundle
        )
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding?.rvCompanies?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CompaniesAdapter(this@CompaniesFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_COMPANY_ID = "extra_company_id"
    }

}