package com.arafat1419.mengantri_app.company.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyCustomersBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.TicketServiceDomain
import com.arafat1419.mengantri_app.core.ui.adapter.TicketsAdapter
import com.arafat1419.mengantri_app.core.utils.LiveDataHelper.observeOnce
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_FRAGMENT_STATUS
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_TICKET_ID
import com.arafat1419.mengantri_app.core.vo.Resource
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


@ExperimentalCoroutinesApi
@FlowPreview
class CompanyCustomersFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyCustomersBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: CompanyHomeViewModel by viewModel()

    // Initialize navHostFragment as fragment
    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(R.id.fragment_container)
    }

    private val getServiceId: Int? by lazy { arguments?.getInt(EXTRA_SERVICE_ID) }
    private val getServiceName: String? by lazy { arguments?.getString(EXTRA_SERVICE_NAME) }

    private val ticketsAdapter: TicketsAdapter by lazy { TicketsAdapter() }

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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyCustomersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtToolbarTitle.text = getServiceName

        setRecyclerView()
        addTabTitle()

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        displayTickets(getString(TAB_TITLE[0]))
        onItemClicked()

        // Manage tab active
        // When tab action it will be display data by tab title
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                displayTickets(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }
        })
    }

    private fun onItemClicked() {
        binding.apply {
            btnBack.setOnClickListener {
                NavHostFragment.findNavController(this@CompanyCustomersFragment).navigateUp()
            }
            btnScan.setOnClickListener {
                navHostFragment?.findNavController()?.navigate(
                    R.id.action_companyCustomersFragment_to_companyScanFragment,
                )
            }

            btnAdd.setOnClickListener {
                val bundle = bundleOf(
                    CompanyAddFragment.EXTRA_SERVICE_ID to getServiceId
                )
                navHostFragment?.findNavController()?.navigate(
                    R.id.action_companyCustomersFragment_to_companyAddFragment,
                    bundle
                )
            }
        }
        ticketsAdapter.onItemClicked = {
            navigateToHome(it)
        }
    }

    // Will display ticket by status
    private fun displayTickets(tabTitle: String) {
        when (tabTitle) {
            getString(TAB_TITLE[0]) -> getTicketsToday()
            getString(TAB_TITLE[1]) -> getTicketsSoon()
            getString(TAB_TITLE[2]) -> getTicketsHistory()
        }
    }

    private fun getTicketsToday() {
        getServiceId?.let { serviceId ->
            viewModel.getTicketsToday(serviceId)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Error -> {
                            isLoading(false)
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            isLoading(true)
                        }
                        is Resource.Success -> {
                            isLoading(false)
                            val listTicket = result.data

                            if (listTicket.isNullOrEmpty()) {
                                isEmpty(true)
                            } else {
                                isEmpty(false)

                                val onlyTicketList = listTicket.map {
                                    it.ticket!!
                                }

                                ticketsAdapter.setData(onlyTicketList)
                                ticketsAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
        }
    }

    private fun getTicketsSoon() {
        getServiceId?.let { serviceId ->
            viewModel.getTicketsSoon(serviceId)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Error -> {
                            isLoading(false)
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            isLoading(true)
                        }
                        is Resource.Success -> {
                            isLoading(false)
                            val listTicket = result.data

                            if (listTicket.isNullOrEmpty()) {
                                isEmpty(true)
                            } else {
                                isEmpty(false)

                                val onlyTicketList = listTicket.map {
                                    it.ticket!!
                                }

                                ticketsAdapter.setData(onlyTicketList)
                                ticketsAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
        }
    }

    private fun getTicketsHistory() {
        getServiceId?.let { serviceId ->
            viewModel.getTicketsHistory(serviceId)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Error -> {
                            isLoading(false)
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> {
                            isLoading(true)
                        }
                        is Resource.Success -> {
                            isLoading(false)
                            val listTicket = result.data

                            if (listTicket.isNullOrEmpty()) {
                                isEmpty(true)
                            } else {
                                isEmpty(false)

                                val onlyTicketList = listTicket.map {
                                    it.ticket!!
                                }

                                ticketsAdapter.setData(onlyTicketList)
                                ticketsAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
        }
    }

    // Add 3 tab by title from companion object
    private fun addTabTitle() {
        binding.tabLayout.apply {
            for (i in TAB_TITLE.indices) {
                addTab(
                    this.newTab()
                        .setText(getString(TAB_TITLE[i]))
                )
            }
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
                rvCustomers.visibility = View.GONE

                empty.root.visibility = View.VISIBLE
                empty.btnAction.visibility = View.GONE
            } else {
                empty.root.visibility = View.GONE
                rvCustomers.visibility = View.VISIBLE
            }
        }
    }

    // Set recycler view with grid and use companies adapter as adapter
    private fun setRecyclerView() {
        binding.rvCustomers.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ticketsAdapter
        }
    }

    private fun navigateToHome(data: TicketServiceDomain) {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.ui.MainActivity")
            ).also {
                it.putExtra(EXTRA_FRAGMENT_STATUS, true)
                it.putExtra(EXTRA_TICKET_ID, data.ticketId)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAB_TITLE = arrayOf(
            com.arafat1419.mengantri_app.assets.R.string.today,
            com.arafat1419.mengantri_app.assets.R.string.soon,
            com.arafat1419.mengantri_app.assets.R.string.history,
        )

        const val EXTRA_SERVICE_ID = "extra_service_id"
        const val EXTRA_SERVICE_NAME = "extra_service_name"
    }
}