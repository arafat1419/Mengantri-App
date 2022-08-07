package com.arafat1419.mengantri_app.home.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.ui.adapter.CompaniesAdapter
import com.arafat1419.mengantri_app.core.vo.Resource
import com.arafat1419.mengantri_app.home.databinding.FragmentSearchCompanyBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.services.ServicesFragment
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
@ObsoleteCoroutinesApi
class SearchCompanyFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentSearchCompanyBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: SearchViewModel by viewModel()

    private val navHostFragment: Fragment? by lazy {
        parentFragment?.parentFragmentManager?.findFragmentById(
            com.arafat1419.mengantri_app.R.id.fragment_container
        )
    }

    private val companiesAdapter: CompaniesAdapter by lazy { CompaniesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchCompanyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        listenKeyword()
        onItemClicked()
    }

    private fun listenKeyword() {
        parentFragment?.setFragmentResultListener(SearchFragment.EXTRA_SEARCH_KEY_COMPANY) { _, bundle ->
            val keyword = bundle.getString(SearchFragment.EXTRA_SEARCH_KEYWORD)
            CoroutineScope(Dispatchers.IO).launch {
                keyword?.let { viewModel.keywordChannel.send(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.companyResult.observe(viewLifecycleOwner, searchObserver)
    }

    private val searchObserver = Observer<Resource<List<CompanyDomain>>> { result ->
        when (result) {
            is Resource.Error -> {
                isLoading(false)
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            is Resource.Loading -> isLoading(true)
            is Resource.Success -> {
                isLoading(false)
                val listCompanies = result.data

                if (listCompanies.isNullOrEmpty()) {
                    isEmpty(true)
                } else {
                    isEmpty(false)
                    companiesAdapter.setData(listCompanies)
                    companiesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun onItemClicked() {
        companiesAdapter.onItemClicked = {
            val bundle = bundleOf(
                ServicesFragment.EXTRA_COMPANY_DOMAIN to it
            )
            navHostFragment?.findNavController()?.navigate(
                com.arafat1419.mengantri_app.R.id.action_searchFragment_to_servicesFragment, bundle
            )
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
                rvSearchCompany.visibility = View.GONE

                empty.root.visibility = View.VISIBLE
                empty.btnAction.visibility = View.GONE
            } else {
                empty.root.visibility = View.GONE
                rvSearchCompany.visibility = View.VISIBLE
            }
        }
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding.rvSearchCompany.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = companiesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}