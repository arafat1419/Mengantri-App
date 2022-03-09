package com.arafat1419.mengantri_app.ticket.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.core.domain.model.TicketWithServiceDomain
import com.arafat1419.mengantri_app.core.ui.AdapterCallback
import com.arafat1419.mengantri_app.core.ui.adapter.TicketsAdapter
import com.arafat1419.mengantri_app.core.utils.StatusHelper
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
class TicketsFragment : Fragment(), AdapterCallback<TicketWithServiceDomain> {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentTicketsBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: TicketsViewModel by viewModel()

    private var navHostFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTicketsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        addTabTitle()

        // Load koin manually for multi modules
        loadKoinModules(ticketsModule)

        // Initialize nav host fragment as fragment container
        navHostFragment = parentFragmentManager.findFragmentById(R.id.fragment_container)

        displayTickets(TAB_TITLE_PROGRESS)

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

    private fun displayTickets(tabTitle: String) {
        when (tabTitle) {
            TAB_TITLE_PROGRESS -> getTicketsByStatus(StatusHelper.TICKET_PROGRESS)
            TAB_TITLE_WAITING -> getTicketsByStatus(StatusHelper.TICKET_WAITING)
            TAB_TITLE_HISTORY -> getTicketsByStatus(TAB_TITLE_HISTORY)
        }
    }

    private fun getTicketsByStatus(ticketStatus: String) {
        if (ticketStatus != TAB_TITLE_HISTORY) {
            viewModel.getTicketByStatus(ticketStatus).observe(viewLifecycleOwner) { listTicket ->
                val sortList = listTicket.sortedBy {
                    it.ticketDate
                }
                binding?.rvTickets?.adapter?.let { adapter ->
                    when (adapter) {
                        is TicketsAdapter -> {
                            adapter.setData(sortList)
                        }
                    }
                }
            }
        } else {
            viewModel.getTicketByStatus(StatusHelper.TICKET_SUCCESS)
                .observe(viewLifecycleOwner) { listTicket ->
                    val mergeTickets = mutableListOf<TicketWithServiceDomain>()
                    listTicket.forEach { ticket ->
                        mergeTickets.add(ticket)
                    }
                    viewModel.getTicketByStatus(StatusHelper.TICKET_CANCEL)
                        .observe(viewLifecycleOwner) { listTicketCancel ->
                            listTicketCancel.forEach { ticket ->
                                mergeTickets.add(ticket)
                            }
                            val sortList = mergeTickets.sortedByDescending {
                                it.ticketDate
                            }
                            binding?.rvTickets?.adapter?.let { adapter ->
                                when (adapter) {
                                    is TicketsAdapter -> {
                                        adapter.setData(sortList)
                                    }
                                }
                            }
                        }
                }
        }
    }

    private fun addTabTitle() {
        binding?.tabLayout?.apply {
            addTab(this.newTab().setText(TAB_TITLE_PROGRESS))
            addTab(this.newTab().setText(TAB_TITLE_WAITING))
            addTab(this.newTab().setText(TAB_TITLE_HISTORY))
        }
    }

    override fun onItemClicked(data: TicketWithServiceDomain) {
        val bundle = bundleOf(
            DetailTicketFragment.EXTRA_TICKET_ID to data.ticketId
        )
        navHostFragment?.findNavController()?.navigate(
            R.id.action_ticketsFragment_to_detailTicketFragment,
            bundle
        )
    }

    private fun setRecyclerView() {
        binding?.rvTickets?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TicketsAdapter(this@TicketsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAB_TITLE_PROGRESS = "Progress"
        const val TAB_TITLE_WAITING = "Waiting"
        const val TAB_TITLE_HISTORY = "History"
    }

}