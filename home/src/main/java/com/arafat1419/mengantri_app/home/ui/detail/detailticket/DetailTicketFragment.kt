package com.arafat1419.mengantri_app.home.ui.detail.detailticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.model.TicketDetailDomain
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

@ExperimentalCoroutinesApi
@FlowPreview
class DetailTicketFragment : Fragment() {
    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentDetailTicketBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: DetailTicketViewModel by viewModel()

    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)
    }

    private val getTicketId: Int? by lazy { arguments?.getInt(EXTRA_TICKET_ID) }

    private val isFromOther: Boolean by lazy {
        arguments?.getBoolean(EXTRA_FROM_OTHER, false) == true
    }

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
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailTicketBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        getDataTicket()
        onItemClicked()
    }

    private fun onItemClicked() {
        binding.apply {
            btnDTicketCancel.setOnClickListener {
                showDialogAlert(
                    getTicketId,
                    StatusHelper.TICKET_CANCEL,
                    getString(R.string.cancel_ticket),
                    resources.getString(R.string.ticket_status_cancelled)
                )
            }

            btnDTicket.setOnClickListener {
                when (btnDTicket.text) {
                    resources.getString(R.string.done) -> {
                        showDialogAlert(
                            getTicketId,
                            StatusHelper.TICKET_SUCCESS,
                            getString(R.string.finish_ticket),
                            resources.getString(R.string.ticket_status_success)
                        )
                    }
                    resources.getString(R.string.process) -> {
                        showDialogAlert(
                            getTicketId,
                            StatusHelper.TICKET_PROGRESS,
                            getString(R.string.process_ticket),
                            resources.getString(R.string.ticket_status_progress)
                        )
                    }
                }
            }

            btnBack.setOnClickListener {
                backToHome()
            }
        }
    }

    private fun getDataTicket() {
        if (getTicketId != null) {
            viewModel.getTicket(getTicketId!!).observe(viewLifecycleOwner) { ticketDetail ->
                if (ticketDetail != null) {
                    if (ticketDetail.ticket?.ticketQrImage.isNullOrEmpty()) {
                        getDataTicket()
                    } else {
                        showDataTicket(ticketDetail)
                        getDataCompany(ticketDetail.ticket?.serviceId?.companyId)
                        statusState(ticketDetail.ticket?.ticketStatus)
                    }
                } else {
                    Toast.makeText(context, R.string.empty_ticket, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getDataCompany(companyId: Int?) {
        if (companyId != null) {
            viewModel.getCompany(companyId).observe(viewLifecycleOwner) { company ->
                if (company != null) {
                    showDataCompany(company)
                }
            }
        }
    }

    private fun showDataCompany(data: CompanyDomain) {
        binding.apply {
            Glide.with(requireContext())
                .load(DataMapper.imageDirectus + data.companyImage)
                .into(imgCompanyProfile)

            txtCompanyId.text = data.companyId?.toString()
            txtCompanyAddress.text = data.companyName
        }
    }

    private fun showDataTicket(data: TicketDetailDomain) {
        binding.apply {
            val ticket = data.ticket
            if (ticket != null) {
                txtToolbarTitle.text = ticket.serviceId?.serviceName

                txtDTicketDay.text = ticket.ticketDate?.let { DateHelper.toUpdateLabel(it) }
                val timeFormat = resources.getString(R.string.time_format)
                txtDTicketTime.text = String.format(
                    timeFormat, ticket.serviceId?.serviceOpenTime?.substring(0..4),
                    ticket.serviceId?.serviceCloseTime?.substring(0..4)
                )
                txtDTicketName.text = ticket.ticketPersonName

                Glide.with(requireContext())
                    .load(DataMapper.imageDirectus + ticket.ticketQrImage)
                    .into(imgDTicketQrCode)
                txtDTicketQueueId.text = ticket.ticketId.toString()

                txtDTicketQueueNumber.text = data.queueNumber
                txtDTicketEst.text = data.estimatedTime

            }
        }
    }

    private fun statusState(ticketStatus: String?) {
        binding.apply {
            when (ticketStatus) {
                StatusHelper.TICKET_WAITING -> {
                    isStatusWaiting(true)
                    if (isFromOther) btnDTicket.visibility = View.VISIBLE
                    btnDTicket.text = resources.getString(R.string.process)
                }
                StatusHelper.TICKET_SUCCESS -> {
                    isStatusWaiting(false)
                    txtDTicketStatus.text = resources.getString(R.string.ticket_status_success)
                }
                StatusHelper.TICKET_PROGRESS -> {
                    isStatusWaiting(false)
                    btnDTicket.text = resources.getString(R.string.done)
                    if (isFromOther) btnDTicket.visibility = View.VISIBLE
                    txtDTicketStatus.text = resources.getString(R.string.ticket_status_progress)
                }
                StatusHelper.TICKET_CANCEL -> {
                    isStatusWaiting(false)
                    txtDTicketStatus.text = resources.getString(R.string.ticket_status_cancelled)
                }
            }
        }
    }

    private fun isStatusWaiting(state: Boolean) {
        binding.apply {
            if (state) {
                titleTicketQueueNumber.visibility = View.VISIBLE
                txtDTicketQueueNumber.visibility = View.VISIBLE
                titleTicketEst.visibility = View.VISIBLE
                txtDTicketEst.visibility = View.VISIBLE
                txtDTicketStatus.visibility = View.GONE
            } else {
                titleTicketQueueNumber.visibility = View.GONE
                txtDTicketQueueNumber.visibility = View.GONE
                titleTicketEst.visibility = View.GONE
                txtDTicketEst.visibility = View.GONE
                txtDTicketStatus.visibility = View.VISIBLE
            }
        }
    }

    private fun showDialogAlert(
        ticketId: Int?, newStatus: String, message: String, ticketStatusMsg: String
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

    private fun backToHome() {
        if (isFromOther) activity?.finish()
        else NavHostFragment.findNavController(this@DetailTicketFragment).navigateUp()
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