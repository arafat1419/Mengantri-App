package com.arafat1419.mengantri_app.home.ui.detail.detailservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.home.databinding.FragmentDetailServiceBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@ExperimentalCoroutinesApi
@FlowPreview
class DetailServiceFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentDetailServiceBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: DetailServiceViewModel by viewModel()

    private var navHostFragment: Fragment? = null

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
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailServiceBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getServiceDomain = arguments?.getParcelable<ServiceDomain>(EXTRA_SERVICE_DOMAIN)
        val companyName = arguments?.getString(EXTRA_COMPANY_NAME)

        if (getServiceDomain != null && companyName != null) {
            showDataService(getServiceDomain, companyName)
        }

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)

        viewModel.getTickets(getServiceDomain?.serviceId!!).observe(viewLifecycleOwner) { listTicket ->
            var served = 0
            var waiting = 0
            var cancel = 0

            listTicket.forEach { ticket ->
                when (ticket.ticketStatus) {
                    "success" -> served++
                    "waiting" -> waiting++
                    "cancel" -> cancel++
                }
            }
            val total = listTicket.size

            showTicketDetail(served, waiting, total, cancel)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    private fun showTicketDetail(served: Int, waiting: Int, total: Int, cancel: Int) {
        binding?.apply {
            txtDServiceServed.text = served.toString()
            txtDServiceWaiting.text = waiting.toString()
            txtDServiceTotal.text = total.toString()
            txtDServiceCancel.text = cancel.toString()
        }
    }

    private fun showDataService(serviceDomain: ServiceDomain, companyName: String) {
        binding?.apply {
            txtServicesAppTitle.text = serviceDomain.serviceName
            txtDServiceTitle.text = serviceDomain.serviceName
            txtDServiceCompany.text = companyName
            txtDServiceTime.text = resources.getString(
                R.string.time_format,
                serviceDomain.serviceOpenTime,
                serviceDomain.serviceCloseTime
            )
            edtDServiceAnnouncement.setText(serviceDomain.serviceAnnouncement)
            txtDServiceEst.text = serviceDomain.serviceTime
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_SERVICE_DOMAIN = "extra_service_domain"
        const val EXTRA_COMPANY_NAME = "extra_company_name"
    }
}