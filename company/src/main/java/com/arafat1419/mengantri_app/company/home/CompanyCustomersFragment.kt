package com.arafat1419.mengantri_app.company.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyCustomersBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.CompanyCustomersAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


@ExperimentalCoroutinesApi
@FlowPreview
class CompanyCustomersFragment : Fragment(), AdapterCallback<TicketDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyCustomersBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: CompanyHomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@CompanyCustomersFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyCustomersBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getServiceId = arguments?.getInt(EXTRA_SERVICE_ID)

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        if (getServiceId != null) {
            viewModel.getTickets(getServiceId).observe(viewLifecycleOwner) {
                binding?.rvCustomers?.adapter.let { adapter ->
                    when (adapter) {
                        is CompanyCustomersAdapter -> {
                            adapter.setData(it)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    override fun onItemClicked(data: TicketDomain) {
        Toast.makeText(context, "Later", Toast.LENGTH_SHORT).show()
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding?.rvCustomers?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = CompanyCustomersAdapter(this@CompanyCustomersFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_SERVICE_ID = "extra_service_id"
    }
}