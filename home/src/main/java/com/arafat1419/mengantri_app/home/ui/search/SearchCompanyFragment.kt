package com.arafat1419.mengantri_app.home.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.CompaniesAdapter
import com.arafat1419.mengantri_app.home.databinding.FragmentSearchCompanyBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
@ObsoleteCoroutinesApi
class SearchCompanyFragment : Fragment(), AdapterCallback<CompanyDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentSearchCompanyBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchCompanyBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        parentFragment?.setFragmentResultListener(SearchFragment.EXTRA_SEARCH_KEYWORD_KEY) { requestKey, bundle ->
            val keyword = bundle.getString(SearchFragment.EXTRA_SEARCH_KEYWORD)
            Log.d("LHTS", keyword.toString())
            CoroutineScope(Dispatchers.IO).launch {
                keyword?.let { viewModel.keywordChannel.send(it) }
            }
        }

        // Load koin manually for multi modules
        loadKoinModules(homeModule)
    }

    override fun onResume() {
        super.onResume()
        viewModel.companyResult.observe(viewLifecycleOwner, searchObserver)
    }

    private val searchObserver = Observer<List<CompanyDomain>> {
        Log.d("LHT", it.toString())
        setRecyclerView()
        binding?.rvSearchCompany?.adapter.let { adapter ->
            when (adapter) {
                is CompaniesAdapter -> {
                    adapter.setData(it)
                }
            }
        }
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding?.rvSearchCompany?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CompaniesAdapter(this@SearchCompanyFragment)
        }
    }

    override fun onItemClicked(data: CompanyDomain) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}