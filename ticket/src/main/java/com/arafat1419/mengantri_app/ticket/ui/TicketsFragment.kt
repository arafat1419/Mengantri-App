package com.arafat1419.mengantri_app.ticket.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.ui.adapter.TicketsAdapter
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.vo.Resource
import com.arafat1419.mengantri_app.home.ui.detail.detailticket.DetailTicketFragment
import com.arafat1419.mengantri_app.ticket.databinding.FragmentTicketsBinding
import com.arafat1419.mengantri_app.ticket.di.ticketsModule
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class TicketsFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: TicketsViewModel by viewModel()

    // Initialize navHostFragment as fragment
    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)
    }

    private val ticketsAdapter: TicketsAdapter by lazy { TicketsAdapter() }

    // Initialize sessionManager as CustomerSessionManager to get customer data
    private val sessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTicketsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        addTabTitle()

        // Load koin manually for multi modules
        loadKoinModules(ticketsModule)

        // This will be displayTicket progress when user open ticket fragment
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

    // Will display ticket by status
    private fun displayTickets(tabTitle: String) {
        when (tabTitle) {
            getString(TAB_TITLE[0]) -> getTicketsWaiting()
            getString(TAB_TITLE[1]) -> getTicketsHistory()
        }
    }

    private fun getTicketsWaiting() {
        viewModel.getTicketsWaiting(sessionManager.fetchCustomerId())
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading(false)
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> isLoading(true)
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

    private fun getTicketsHistory() {
        viewModel.getTicketsHistory(sessionManager.fetchCustomerId())
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

    // Add 2 tab by title from companion object
    private fun addTabTitle() {
        binding.tabLayout.apply {
            for (i in TAB_TITLE.indices) {
                addTab(
                    this.newTab()
                        .setText(getString(TAB_TITLE[i]))
                        .setIcon(ContextCompat.getDrawable(requireContext(), TAB_ICON[i]))
                )
            }
        }
    }

    // when ticket is clicked it will navigate to DetailTicketFragment with ticketWithServiceDomain
    private fun onItemClicked() {
        ticketsAdapter.onItemClicked = {
            val bundle = bundleOf(
                DetailTicketFragment.EXTRA_TICKET_ID to it.ticketId
            )
            navHostFragment?.findNavController()?.navigate(
                com.arafat1419.mengantri_app.R.id.action_ticketsFragment_to_detailTicketFragment,
                bundle
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
                rvTickets.visibility = View.GONE

                empty.root.visibility = View.VISIBLE
                empty.btnAction.apply {
                    text = getString(R.string.search_service)
                    setOnClickListener {
                        navHostFragment?.findNavController()?.navigate(
                            com.arafat1419.mengantri_app.R.id.action_ticketsFragment_to_searchFragment
                        )
                    }
                }
            } else {
                empty.root.visibility = View.GONE
                rvTickets.visibility = View.VISIBLE
            }
        }
    }

    // Set recycler view
    private fun setRecyclerView() {
        binding.rvTickets.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ticketsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAB_TITLE = arrayOf(
            R.string.waiting,
            R.string.history
        )

        private val TAB_ICON = arrayOf(
            R.drawable.ic_outline_hourglass_empty_24,
            R.drawable.ic_outline_history_24
        )
    }

}