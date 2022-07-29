package com.arafat1419.mengantri_app.home.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.ui.adapter.ServicesAdapter
import com.arafat1419.mengantri_app.home.databinding.FragmentSearchServiceBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.detail.detailservice.DetailServiceFragment
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
@ObsoleteCoroutinesApi
class SearchServiceFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentSearchServiceBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: SearchViewModel by viewModel()

    private val navHostFragment: Fragment? by lazy { parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container) }

    private val servicesAdapter: ServicesAdapter by lazy { ServicesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchServiceBinding.inflate(layoutInflater, container, false)
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
        parentFragment?.setFragmentResultListener(SearchFragment.EXTRA_SEARCH_KEYWORD_KEY) { _, bundle ->
            val keyword = bundle.getString(SearchFragment.EXTRA_SEARCH_KEYWORD)
            CoroutineScope(Dispatchers.IO).launch {
                keyword?.let { viewModel.keywordChannel.send(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.serviceResult.observe(viewLifecycleOwner, searchObserver)
    }

    private val searchObserver = Observer<List<ServiceCountDomain>> { listServiceCount ->
        if (!listServiceCount.isNullOrEmpty()) {
            servicesAdapter.setData(listServiceCount)
            servicesAdapter.notifyDataSetChanged()
        }
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding.rvSearchService.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = servicesAdapter
        }
    }

    private fun onItemClicked() {
        servicesAdapter.onItemClicked = {
            val bundle = bundleOf(
                DetailServiceFragment.EXTRA_SERVICE_ID to it.service?.serviceId
            )
            navHostFragment?.findNavController()?.navigate(
                com.arafat1419.mengantri_app.R.id.action_searchServiceFragment_to_servicesFragment,
                bundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}