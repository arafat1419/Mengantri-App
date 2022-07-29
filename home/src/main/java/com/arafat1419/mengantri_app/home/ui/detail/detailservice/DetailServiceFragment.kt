package com.arafat1419.mengantri_app.home.ui.detail.detailservice

import android.app.DatePickerDialog
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
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.utils.DataMapper
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.arafat1419.mengantri_app.home.databinding.FragmentDetailServiceBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import com.arafat1419.mengantri_app.home.ui.detail.detailticket.DetailTicketFragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalCoroutinesApi
@FlowPreview
class DetailServiceFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentDetailServiceBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: DetailServiceViewModel by viewModel()

    private val navHostFragment: Fragment? by lazy {
        parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)
    }

    private val getServiceId by lazy { arguments?.getInt(EXTRA_SERVICE_ID) }

    private val customerSessionManager by lazy { CustomerSessionManager(requireContext()) }

    private var ticketDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@DetailServiceFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailServiceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        getAndShowServiceCounted()
    }

    private fun onItemClicked(serviceCountDomain: ServiceCountDomain?) {
        binding.apply {
            edtDServiceDate.setOnClickListener {
                showDateDialog(serviceCountDomain)
            }

            btnDServiceRegister.setOnClickListener {
                if (!edtDServiceDate.text.isNullOrEmpty()) {
                    postTicket(serviceCountDomain)
                } else {
                    Toast.makeText(context, R.string.date_cannot_empty, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAndShowServiceCounted() {
        if (getServiceId != null) {
            viewModel.getServiceCounted(getServiceId!!)
                .observe(viewLifecycleOwner) { serviceCount ->
                    binding.apply {
                        txtToolbarTitle.text = serviceCount.service?.serviceName
                        edtDServiceAnnouncement.setText(serviceCount.service?.serviceAnnouncement)

                        txtDServiceServed.text = serviceCount.served.toString()
                        txtDServiceWaiting.text = serviceCount.waiting.toString()
                        txtDServiceTotal.text = serviceCount.total.toString()
                        txtDServiceCancel.text = serviceCount.cancel.toString()

                        getAndShowDataCompany(serviceCount.service?.companyId)
                        getEstimatedTime { txtDServiceEst.text = it }

                        onItemClicked(serviceCount)
                    }
                }
        }
    }

    private fun getAndShowDataCompany(companyId: Int?) {
        if (companyId != null) {
            viewModel.getCompany(companyId).observe(viewLifecycleOwner) { company ->
                binding.apply {
                    Glide.with(this@DetailServiceFragment)
                        .load(DataMapper.imageDirectus + company.companyImage)
                        .into(imgCompanyImage)

                    txtCompanyId.text = company.companyId.toString()
                    txtCompanyName.text = company.companyName
                    txtCompanyAddress.text = company.companyAddress
                }
            }
        }
    }

    private fun getEstimatedTime(onEstimatedTimeFound: (String) -> Unit) {
        if (getServiceId != null) {
            viewModel.getEstimatedTime(getServiceId!!).observe(viewLifecycleOwner) {
                if (it != null) {
                    onEstimatedTimeFound(it)
                }
            }
        }
    }

    private fun postTicket(serviceCountDomain: ServiceCountDomain?) {
        var estimatedTime = ""
        getEstimatedTime { estimatedTime = it }
        viewModel.postTicket(
            customerSessionManager.fetchCustomerId(),
            serviceCountDomain?.service?.serviceId!!,
            customerSessionManager.fetchCustomerName()!!,
            customerSessionManager.fetchCustomerPhone()!!,
            "-",
            ticketDate,
            estimatedTime,
        ).observe(viewLifecycleOwner) { ticket ->
            Toast.makeText(context, R.string.ticket_status_added, Toast.LENGTH_SHORT)
                .show()
            val bundle = bundleOf(DetailTicketFragment.EXTRA_TICKET_ID to ticket.ticketId)
            navHostFragment?.findNavController()?.navigate(
                com.arafat1419.mengantri_app.R.id.action_detailServiceFragment_to_detailTicketFragment,
                bundle
            )
        }
    }

    private fun showDateDialog(serviceCountDomain: ServiceCountDomain?) {
        val myCalendar = Calendar.getInstance()
        myCalendar.firstDayOfWeek = Calendar.MONDAY
        var minDate = myCalendar.timeInMillis - 1000

        val timeFormatter = SimpleDateFormat("HH:mm:ss")

        val closeTime = serviceCountDomain?.service?.serviceCloseTime
        val currentTime = timeFormatter.format(myCalendar.time)

        if (currentTime > closeTime!!) {
            myCalendar.add(Calendar.DAY_OF_MONTH, 1)
            minDate = myCalendar.timeInMillis - 1000
        }

        val date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            myCalendar.set(year, month, day)
            binding.edtDServiceDate.setText(DateHelper.updateLabel(myCalendar))
            ticketDate = DateHelper.returnLabel(myCalendar)
            checkAvailabilityDay(
                serviceCountDomain.service?.serviceDay,
                myCalendar[Calendar.DAY_OF_WEEK]
            )
        }
        val datePicker = DatePickerDialog(
            requireContext(),
            date,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH],
        )
        datePicker.datePicker.minDate = minDate

        datePicker.show()
    }

    private fun checkAvailabilityDay(serviceDay: List<String>?, dayId: Int) {
        binding.apply {
            if (serviceDay?.contains(dayId.toString()) == true) {
                btnDServiceRegister.isEnabled = true
            } else {
                Toast.makeText(context, R.string.please_choose_other_date, Toast.LENGTH_SHORT)
                    .show()
                btnDServiceRegister.isEnabled = false
            }
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