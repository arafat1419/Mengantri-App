package com.arafat1419.mengantri_app.company.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyAddBinding
import com.arafat1419.mengantri_app.core.domain.model.EstimatedTimeDomain
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import com.arafat1419.mengantri_app.core.vo.Resource
import org.koin.android.viewmodel.ext.android.viewModel

class CompanyAddFragment : Fragment() {

    private var _binding: FragmentCompanyAddBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: CompanyHomeViewModel by viewModel()

    private val customerSessionManager by lazy { CustomerSessionManager(requireContext()) }

    private val getServiceId by lazy { arguments?.getInt(EXTRA_SERVICE_ID) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@CompanyAddFragment).navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onItemClick()
    }

    private fun onItemClick() {
        binding.apply {
            btnSubmit.setOnClickListener {
                if (!isEditTextNull()) {
                    postTicket(
                        edtName.text.toString(),
                        edtPhone.text.toString(),
                        edtNotes.text.toString()
                    )
                }
            }
            btnBack.setOnClickListener {
                NavHostFragment.findNavController(this@CompanyAddFragment).navigateUp()
            }
        }
    }

    private fun getEstimatedTime(
        ticketDate: String,
        onEstimatedTimeFound: (EstimatedTimeDomain) -> Unit
    ) {
        if (getServiceId != null) {
            viewModel.getEstimatedTime(getServiceId!!, ticketDate)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Error -> {
                            isLoading(false)
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> isLoading(true)
                        is Resource.Success -> {
                            isLoading(false)
                            val estimatedTime = result.data

                            if (estimatedTime != null) {
                                onEstimatedTimeFound(estimatedTime)
                            }
                        }
                    }
                }
        }
    }

    private fun postTicket(name: String, phone: String, notes: String) {
        val todayDate = DateHelper.getTodayDatePost()
        getEstimatedTime(todayDate) { estimatedTime ->
            if (estimatedTime.isAvailable == true) {
                viewModel.postTicket(
                    customerSessionManager.fetchCustomerId(),
                    getServiceId!!,
                    name,
                    phone,
                    notes,
                    todayDate,
                    estimatedTime.estimatedTime!!,
                ).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Resource.Error -> {
                            isLoading(false)
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Loading -> isLoading(true)
                        is Resource.Success -> {
                            isLoading(false)
                            val ticket = result.data

                            Toast.makeText(
                                context,
                                R.string.ticket_status_added,
                                Toast.LENGTH_SHORT
                            ).show()
                            if (ticket != null) {
                                navigateToHome(ticket)
                                NavHostFragment.findNavController(this@CompanyAddFragment)
                                    .navigateUp()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.ticket_not_available),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isLoading(state: Boolean) {
        binding.loading.root.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun isEditTextNull(): Boolean {
        var isNullOrEmpty: Boolean
        binding.apply {
            isNullOrEmpty = edtName.text.isNullOrEmpty() || edtPhone.text.isNullOrEmpty()
        }
        if (isNullOrEmpty) Toast.makeText(
            context,
            getString(R.string.field_cannot_empty),
            Toast.LENGTH_SHORT
        ).show()
        return isNullOrEmpty
    }

    private fun navigateToHome(data: TicketDomain) {
        // Navigate to MainActivity in app module and destroy this activity parent for reduce memory consumption
        try {
            Intent(
                requireActivity(),
                Class.forName("com.arafat1419.mengantri_app.ui.MainActivity")
            ).also {
                it.putExtra(StatusHelper.EXTRA_FRAGMENT_STATUS, true)
                it.putExtra(StatusHelper.EXTRA_TICKET_ID, data.ticketId)
                startActivity(it)
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                R.string.module_not_found,
                Toast.LENGTH_SHORT
            ).show()
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