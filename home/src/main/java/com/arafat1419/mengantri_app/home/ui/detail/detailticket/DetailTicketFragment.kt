package com.arafat1419.mengantri_app.home.ui.detail.detailticket

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.domain.model.TicketWithServiceDomain
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import com.arafat1419.mengantri_app.home.databinding.FragmentDetailTicketBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class DetailTicketFragment : Fragment() {
    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentDetailTicketBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: DetailTicketViewModel by viewModel()

    private var navHostFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    backToHome()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailTicketBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ticketId = arguments?.getInt(EXTRA_TICKET_ID)

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)

        viewModel.getTicket(ticketId!!).observe(viewLifecycleOwner) { listTicketWithService ->
            if (listTicketWithService.isNotEmpty()) {
                showData(listTicketWithService[0])
            } else {
                Toast.makeText(context, "Empty ticket", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.apply {
            btnDTicketCancel.setOnClickListener {
                showDialogAlert(ticketId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    private fun showData(data: TicketWithServiceDomain) {
        binding?.apply {
            if (data.serviceId != null) {
                val serviceDomain = data.serviceId!!
                txtServicesAppTitle.text = serviceDomain.serviceName
                txtDTicketTitle.text = serviceDomain.serviceName
                txtDTicketCompany.text = serviceDomain.companyId?.companyName
            }
            txtDTicketDay.text = DateHelper.toUpdateLabel(data.ticketDate!!)
            val timeFormat = resources.getString(R.string.time_format)
            txtDTicketTime.text = String.format(
                timeFormat, data.serviceId?.serviceOpenTime?.substring(0..4),
                data.serviceId?.serviceCloseTime?.substring(0..4)
            )
            txtDTicketEst.text = data.ticketServiceTime
            txtDTicketName.text = data.ticketPersonName
            txtDTicketQueueId.text = data.ticketId.toString()
            statusState(data)
        }
    }

    private fun statusState(data: TicketWithServiceDomain) {
        binding?.apply {
            when (data.ticketStatus) {
                StatusHelper.TICKET_WAITING -> {
                    statusNotWaiting(false)
                    txtDTicketEst.text = data.ticketServiceTime

                    viewModel.getTickets(data.serviceId?.serviceId!!).observe(viewLifecycleOwner) { listTicket ->
                        var queueNumber = 0
                        listTicket.forEach { ticketDomain ->
                            if (ticketDomain.ticketStatus == StatusHelper.TICKET_WAITING && ticketDomain.ticketId!! < data.ticketId!!) {
                                queueNumber++
                            }
                        }

                        txtDTicketQueueNumber.text = if (queueNumber == 0) "Your turn" else queueNumber.toString()
                    }
                }
                StatusHelper.TICKET_SUCCESS -> {
                    statusNotWaiting(true)
                    txtDTicketStatus.text = resources.getString(R.string.ticket_status_success)
                }
                StatusHelper.TICKET_PROGRESS -> {
                    statusNotWaiting(true)
                    txtDTicketStatus.text = resources.getString(R.string.ticket_status_progress)
                }
                StatusHelper.TICKET_CANCEL -> {
                    statusNotWaiting(true)
                    txtDTicketStatus.text = resources.getString(R.string.ticket_status_cancelled)
                }
            }
        }
    }

    private fun statusNotWaiting(state: Boolean) {
        binding?.apply {
            if (state) {
                txtDTicketStatus.visibility = View.VISIBLE

                titleTicketQueueNumber.visibility = View.GONE
                txtDTicketQueueNumber.visibility = View.GONE
                titleTicketEst.visibility = View.GONE
                txtDTicketEst.visibility = View.GONE
                btnDTicketCancel.visibility = View.GONE
            } else {
                titleTicketQueueNumber.visibility = View.VISIBLE
                txtDTicketQueueNumber.visibility = View.VISIBLE
                titleTicketEst.visibility = View.VISIBLE
                txtDTicketEst.visibility = View.VISIBLE
                btnDTicketCancel.visibility = View.VISIBLE

                txtDTicketStatus.visibility = View.GONE
            }
        }
    }

    private fun showDialogAlert(ticketId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cancel Ticket?")
        builder.setMessage(R.string.ticket_cancel_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                Log.d("Lihat Dialog", "Yes")
                viewModel.updateTicket(ticketId, StatusHelper.TICKET_CANCEL).observe(viewLifecycleOwner) { ticketDomain ->
                    Log.d("Lihat detail dialog", ticketDomain.toString())
                    if (ticketDomain != null) {
                        Toast.makeText(context, "Ticket has been cancelled", Toast.LENGTH_SHORT).show()
                        backToHome()
                    }
                }
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        builder.create()
        builder.show()
    }

    private fun backToHome() {
        NavHostFragment.findNavController(this@DetailTicketFragment).navigateUp()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_TICKET_ID = "extra_ticket_id"
    }
}