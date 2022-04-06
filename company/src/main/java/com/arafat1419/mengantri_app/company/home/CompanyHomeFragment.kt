package com.arafat1419.mengantri_app.company.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyHomeBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.ServicesAdapter
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyHomeFragment : Fragment(), AdapterCallback<ServiceCountDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyHomeBinding? = null
    private val binding get() = _binding

    private lateinit var companySessionManager: CompanySessionManager

    // Initialize viewModel with koin
    private val viewModel: CompanyHomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        setRecyclerView()

        companySessionManager = CompanySessionManager(requireContext())

        viewModel.getServiceAndServed(companySessionManager.fetchCompanyId()).observe(viewLifecycleOwner) {
            binding?.rvCompanyServices?.adapter.let { adapter ->
                when (adapter) {
                    is ServicesAdapter -> {
                        adapter.setData(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onItemClicked(data: ServiceCountDomain) {
        Toast.makeText(context, "Later", Toast.LENGTH_SHORT).show()
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding?.rvCompanyServices?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ServicesAdapter(this@CompanyHomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}