package com.arafat1419.mengantri_app.home.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.CompaniesAdapter
import com.arafat1419.mengantri_app.home.databinding.FragmentSearchBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.services.ServicesFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
@ObsoleteCoroutinesApi
class SearchFragment : Fragment(), AdapterCallback<CompanyDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: SearchViewModel by viewModel()

    private var navHostFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@SearchFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)

        val getCategoryId = arguments?.getInt(EXTRA_CATEGORY_ID)
        viewModel.categoryId = getCategoryId

        binding?.edtSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.companyResult.observe(viewLifecycleOwner, searchObserver)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                CoroutineScope(IO).launch {
                    viewModel.keywordChannel.send(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun onItemClicked(data: CompanyDomain) {
        val bundle = bundleOf(
            ServicesFragment.EXTRA_COMPANY_DOMAIN to data
        )
        navHostFragment?.findNavController()?.navigate(
            com.arafat1419.mengantri_app.R.id.action_searchFragment_to_servicesFragment, bundle
        )
    }

    private val searchObserver = Observer<List<CompanyDomain>> {
        setRecyclerView()
        binding?.rvSearch?.adapter.let { adapter ->
            when (adapter) {
                is CompaniesAdapter -> {
                    adapter.setData(it)
                }
            }
        }
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding?.rvSearch?.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CompaniesAdapter(this@SearchFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
    }
}