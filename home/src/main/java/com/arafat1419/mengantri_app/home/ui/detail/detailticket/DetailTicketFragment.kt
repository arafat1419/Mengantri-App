package com.arafat1419.mengantri_app.home.ui.detail.detailticket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.domain.model.TicketWithServiceDomain
import com.arafat1419.mengantri_app.core.utils.DataMapper
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import com.arafat1419.mengantri_app.home.databinding.FragmentDetailTicketBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
class DetailTicketFragment : Fragment() {
    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentDetailTicketBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: DetailTicketViewModel by viewModel()

    private var isFromOther: Boolean = false

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

        isFromOther = arguments?.getBoolean(EXTRA_FROM_OTHER, false) == true

        if (isFromOther) {
            // This callback will only be called when MyFragment is at least Started.
            val callback: OnBackPressedCallback =
                object : OnBackPressedCallback(true /* enabled by default */) {
                    override fun handleOnBackPressed() {
                        // Handle the back button event
                        activity?.finish()
                    }
                }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        } else binding?.btnDTicket?.visibility = View.GONE

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)

        binding?.apply {
            btnDTicketCancel.setOnClickListener {
                showDialogAlert(
                    ticketId,
                    StatusHelper.TICKET_CANCEL,
                    getString(R.string.cancel_ticket),
                    resources.getString(R.string.ticket_status_cancelled)
                )
            }

            btnDTicket.setOnClickListener {
                when (btnDTicket.text) {
                    resources.getString(R.string.done) -> {
                        showDialogAlert(
                            ticketId,
                            StatusHelper.TICKET_SUCCESS,
                            getString(R.string.finish_ticket),
                            resources.getString(R.string.ticket_status_success)
                        )
                    }
                    resources.getString(R.string.process) -> {
                        showDialogAlert(
                            ticketId,
                            StatusHelper.TICKET_PROGRESS,
                            getString(R.string.process_ticket),
                            resources.getString(R.string.ticket_status_progress)
                        )
                    }
                }
            }
        }

        ticketId?.let { getData(it) }
    }

    private fun getData(ticketId: Int) {
        viewModel.getTicket(ticketId).observe(viewLifecycleOwner) { listTicketWithService ->
            if (listTicketWithService.isNotEmpty()) {
                if (listTicketWithService[0].ticketQrImage.isNullOrEmpty()) {
                    getData(ticketId)
                } else {
                    showData(listTicketWithService[0])
                }
            } else {
                Toast.makeText(context, R.string.empty_ticket, Toast.LENGTH_SHORT).show()
            }
        }
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
            txtDTicketName.text = data.ticketPersonName
            txtDTicketQueueId.text = data.ticketId.toString()
            Glide.with(requireContext())
                .load(DataMapper.imageDirectus + data.ticketQrImage)
                .into(imgDTicketQrCode)
            statusState(isFromOther, data)
        }
    }

    private fun statusState(isFromOther: Boolean, data: TicketWithServiceDomain) {
        binding?.apply {
            when (data.ticketStatus) {
                StatusHelper.TICKET_WAITING -> {

                    statusNotWaiting(false)
                    if (isFromOther) {
                        btnDTicket.visibility = View.VISIBLE
                        btnDTicket.text = resources.getString(R.string.process)
                        btnDTicketCancel.visibility = View.VISIBLE
                    }

                    viewModel.getTickets(data.serviceId?.serviceId!!, data.ticketDate)
                        .observe(viewLifecycleOwner) { listTicket ->
                            var queueNumber = 0
                            var estNumber = 0
                            var counter = 0

                            val listTicketsToProcess = listTicket.filter {
                                counter++
                                (it.ticketStatus == StatusHelper.TICKET_WAITING && counter <= data.serviceId?.serviceMaxCustomer!!)
                            }

                            val ticketsToProcess =
                                listTicketsToProcess.find { ticketDomain -> ticketDomain.ticketId == data.ticketId }

                            listTicket.forEach { ticketDomain ->
                                if (ticketDomain.ticketStatus == StatusHelper.TICKET_PROGRESS) btnDTicket.visibility =
                                    View.GONE
                                if (ticketDomain.ticketStatus == StatusHelper.TICKET_WAITING && ticketDomain.ticketId!! < data.ticketId!!) {
                                    queueNumber++
                                }
                                if (ticketDomain.ticketStatus != StatusHelper.TICKET_CANCEL && ticketDomain.ticketId!! < data.ticketId!!) {
                                    estNumber++
                                }
                            }

                            txtDTicketEst.text = countEstServiceTime(
                                data.serviceId?.serviceOpenTime!!,
                                data.serviceId?.serviceTime,
                                estNumber
                            )
                            txtDTicketQueueNumber.text =
                                if (queueNumber == 0 && isSameDay(data.ticketDate)) {
                                    getString(R.string.your_turn)
                                } else if (ticketsToProcess?.ticketId == data.ticketId && !isSameDay(
                                        data.ticketDate
                                    )
                                ) {
                                    btnDTicket.visibility = View.GONE
                                    getString(R.string.first_queue)
                                } else {
                                    btnDTicket.visibility =
                                        if (ticketsToProcess != null && isFromOther) View.VISIBLE else View.GONE
                                    queueNumber.toString()
                                }
                        }
                }
                StatusHelper.TICKET_SUCCESS -> {
                    statusNotWaiting(true)
                    txtDTicketStatus.text = resources.getString(R.string.ticket_status_success)
                }
                StatusHelper.TICKET_PROGRESS -> {
                    statusNotWaiting(true)
                    if (isFromOther) {
                        btnDTicket.visibility = View.VISIBLE
                        btnDTicket.text = resources.getString(R.string.done)
                        btnDTicketCancel.visibility = View.VISIBLE
                    }
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

    private fun showDialogAlert(
        ticketId: Int?,
        newStatus: String,
        message: String,
        ticketStatusMsg: String
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton(R.string.yes) { _, _ ->
                ticketId?.let {
                    viewModel.updateTicket(it, newStatus)
                        .observe(viewLifecycleOwner) { ticketDomain ->
                            if (ticketDomain != null) {
                                Toast.makeText(context, ticketStatusMsg, Toast.LENGTH_SHORT)
                                    .show()
                                backToHome()
                            }
                        }
                }
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        builder.create()
        builder.show()
    }

    private fun countEstServiceTime(openTime: String, estTime: String?, queue: Int): String {
        val myCalendar = Calendar.getInstance()
        val timeFormatter = SimpleDateFormat("HH:mm:ss")
        val currentTime = timeFormatter.format(myCalendar.time)

        val newOpenTime =
            DateHelper.stringTimeToInt(if (currentTime > openTime) currentTime else openTime)

        myCalendar.set(Calendar.HOUR_OF_DAY, newOpenTime[DateHelper.HOURS]!!)
        myCalendar.set(Calendar.MINUTE, newOpenTime[DateHelper.MINUTES]!!)
        myCalendar.set(Calendar.SECOND, newOpenTime[DateHelper.SECONDS]!!)

        if (queue > 0) {
            for (i in 1..queue) {
                val newEstTime = estTime?.let { DateHelper.stringTimeToInt(it) }
                myCalendar.add(Calendar.HOUR_OF_DAY, newEstTime?.get(DateHelper.HOURS)!!)
                myCalendar.add(Calendar.MINUTE, newEstTime[DateHelper.MINUTES]!!)
                myCalendar.add(Calendar.SECOND, newEstTime[DateHelper.SECONDS]!!)
            }
        }

        return timeFormatter.format(myCalendar.time)
    }

    private fun isSameDay(ticketDate: String?): Boolean {
        val df: DateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate: String = df.format(Date())

        return currentDate == ticketDate
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
        const val EXTRA_FROM_OTHER = "extra_from_other"
    }
}