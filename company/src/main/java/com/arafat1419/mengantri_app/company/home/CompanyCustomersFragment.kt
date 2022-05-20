package com.arafat1419.mengantri_app.company.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyCustomersBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.CompanyCustomersAdapter
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_FRAGMENT_STATUS
import com.arafat1419.mengantri_app.core.utils.StatusHelper.EXTRA_TICKET_ID
import com.google.android.material.tabs.TabLayout
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

    private var serviceId: Int? = null

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

        serviceId = arguments?.getInt(EXTRA_SERVICE_ID)
        val getServiceName = arguments?.getString(EXTRA_SERVICE_NAME)

        binding?.txtServicesAppTitle?.text = getServiceName

        setRecyclerView()

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        addTabTitle()

        getTicketsByType(TAB_TITLE_TODAY)

        // Manage tab active
        // When tab action it will be display data by tab title
        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onItemClicked(data: TicketDomain) {
        navigateToHome(data)
    }

    private fun navigateToHome(data: TicketDomain) {
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
            Toast.makeText(context, "Module not found", Toast.LENGTH_SHORT).show()
        }
    }

    // Will display ticket by status
    private fun displayTickets(tabTitle: String) {
        when (tabTitle) {
            TAB_TITLE_TODAY -> getTicketsByType(TAB_TITLE_TODAY)
            TAB_TITLE_SOON -> getTicketsByType(TAB_TITLE_SOON)
            TAB_TITLE_HISTORY -> getTicketsByType(TAB_TITLE_HISTORY)
        }
    }

    private fun getTicketsByType(ticketType: String) {
        // If ticket is not history then this will be sort ticket from newest
        if (serviceId == null) return
        when(ticketType) {
            TAB_TITLE_TODAY -> {
                viewModel.getTickets(serviceId!!).observe(viewLifecycleOwner) { listTicket ->
                    val list = listTicket.sortedWith(compareBy<TicketDomain> {
                        val statusInInt = when(it.ticketStatus) {
                            StatusHelper.TICKET_PROGRESS -> 0
                            StatusHelper.TICKET_WAITING -> 1
                            else -> {2}
                        }
                        statusInInt
                    }.thenBy { it.ticketId })
                    binding?.rvCustomers?.adapter.let { adapter ->
                        when (adapter) {
                            is CompanyCustomersAdapter -> {
                                adapter.setData(list)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
            TAB_TITLE_SOON -> {
                viewModel.getTicketsSoon(serviceId!!).observe(viewLifecycleOwner) { listTicket ->
                    val list = listTicket.sortedWith(compareBy<TicketDomain> {
                        val statusInInt = when(it.ticketStatus) {
                            StatusHelper.TICKET_PROGRESS -> 0
                            StatusHelper.TICKET_WAITING -> 1
                            else -> {2}
                        }
                        statusInInt
                    }.thenBy { it.ticketId })
                    binding?.rvCustomers?.adapter.let { adapter ->
                        when (adapter) {
                            is CompanyCustomersAdapter -> {
                                adapter.setData(list)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
            TAB_TITLE_HISTORY -> {
                viewModel.getTicketsByService(serviceId!!).observe(viewLifecycleOwner) { listTicket ->
                    val list = listTicket.sortedWith(compareBy<TicketDomain> {
                        val statusInInt = when(it.ticketStatus) {
                            StatusHelper.TICKET_PROGRESS -> 0
                            StatusHelper.TICKET_WAITING -> 1
                            else -> {2}
                        }
                        statusInInt
                    }.thenBy { it.ticketId })
                    val dropProgress =  list.dropWhile {
                        it.ticketStatus == StatusHelper.TICKET_PROGRESS || it.ticketStatus == StatusHelper.TICKET_WAITING
                    }
                    binding?.rvCustomers?.adapter.let { adapter ->
                        when (adapter) {
                            is CompanyCustomersAdapter -> {
                                adapter.setData(dropProgress)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }

    // Add 3 tab by title from companion object
    private fun addTabTitle() {
        binding?.tabLayout?.apply {
            addTab(this.newTab().setText(TAB_TITLE_TODAY))
            addTab(this.newTab().setText(TAB_TITLE_SOON))
            addTab(this.newTab().setText(TAB_TITLE_HISTORY))
        }
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
        const val EXTRA_SERVICE_NAME = "extra_service_name"

        const val TAB_TITLE_TODAY = "Today"
        const val TAB_TITLE_SOON = "Soon"
        const val TAB_TITLE_HISTORY = "History"
    }
}