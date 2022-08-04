package com.arafat1419.mengantri_app.company.services

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyDetailServiceBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.arafat1419.mengantri_app.core.utils.StatusHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyDetailServiceFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyDetailServiceBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: CompanyServicesViewModel by viewModel()

    private val companySessionManager by lazy { CompanySessionManager(requireContext()) }

    private val getServiceId: Int? by lazy { arguments?.getInt(EXTRA_SERVICE_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    NavHostFragment.findNavController(this@CompanyDetailServiceFragment)
                        .navigateUp()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyDetailServiceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        onItemClicked()

        binding.apply {
            if (getServiceId == null) {
                // Add service
                txtToolbarTitle.text = getString(R.string.add_service)
            } else {
                // Update service
                txtToolbarTitle.text = getString(R.string.edit_profile)
                getService()

                btnSubmit.visibility = View.VISIBLE
                txtShowService.visibility = View.VISIBLE
                swcServiceShow.visibility = View.VISIBLE

                btnAddService.visibility = View.GONE
            }
        }
    }

    private fun getService() {
        viewModel.getService(getServiceId!!).observe(viewLifecycleOwner) { service ->
            if (service != null) {
                showData(service)
            }
        }
    }

    private fun showData(serviceDomain: ServiceDomain) {
        binding.apply {
            edtServiceName.setText(serviceDomain.serviceName)
            edtCpOpen.setText(serviceDomain.serviceOpenTime?.substring(0..4))
            edtCpClose.setText(serviceDomain.serviceCloseTime?.substring(0..4))
            edtServiceMax.setText(serviceDomain.serviceMaxCustomer.toString())
            edtServiceCashier.setText(serviceDomain.serviceCashier.toString())

            serviceDomain.serviceDay?.forEach {
                when (it) {
                    StatusHelper.DAY_SUNDAY.toString() -> chkOpenSunday.isChecked = true
                    StatusHelper.DAY_MONDAY.toString() -> chkOpenMonday.isChecked = true
                    StatusHelper.DAY_TUESDAY.toString() -> chkOpenTuesday.isChecked = true
                    StatusHelper.DAY_WEDNESDAY.toString() -> chkOpenWednesday.isChecked = true
                    StatusHelper.DAY_THURSDAY.toString() -> chkOpenThursday.isChecked = true
                    StatusHelper.DAY_FRIDAY.toString() -> chkOpenFriday.isChecked = true
                    StatusHelper.DAY_SATURDAY.toString() -> chkOpenSaturday.isChecked = true
                }
            }

            edtDServiceAnnouncement.setText(serviceDomain.serviceAnnouncement)

            swcServiceShow.isChecked = serviceDomain.serviceStatus != 0
        }
    }

    private fun postService() {
        binding.apply {
            if (!isEditTextNull()) {
                viewModel.postService(
                    ServiceDomain(
                        companyId = companySessionManager.fetchCompanyId(),
                        serviceName = edtServiceName.text.toString(),
                        serviceOpenTime = edtCpOpen.text.toString(),
                        serviceCloseTime = edtCpClose.text.toString(),
                        serviceAnnouncement = edtDServiceAnnouncement.text.toString(),
                        serviceMaxCustomer = edtServiceMax.text.toString().toInt(),
                        serviceCashier = edtServiceCashier.text.toString().toInt(),
                        serviceStatus = if (swcServiceShow.isChecked) 1 else 0,
                        serviceDay = getListFromDays()
                    )
                ).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        Toast.makeText(
                            context,
                            getString(R.string.add_service_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        NavHostFragment.findNavController(this@CompanyDetailServiceFragment)
                            .navigateUp()
                    }
                }
            }
        }
    }

    private fun updateService(serviceId: Int) {
        binding.apply {
            viewModel.updateService(
                serviceId,
                ServiceDomain(
                    companyId = companySessionManager.fetchCompanyId(),
                    serviceName = edtServiceName.text.toString(),
                    serviceOpenTime = edtCpOpen.text.toString(),
                    serviceCloseTime = edtCpClose.text.toString(),
                    serviceAnnouncement = edtDServiceAnnouncement.text.toString(),
                    serviceMaxCustomer = edtServiceMax.text.toString().toInt(),
                    serviceCashier = edtServiceCashier.text.toString().toInt(),
                    serviceStatus = if (swcServiceShow.isChecked) 1 else 0,
                    serviceDay = getListFromDays()
                )
            ).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    Toast.makeText(
                        context,
                        getString(R.string.update_service_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    NavHostFragment.findNavController(this@CompanyDetailServiceFragment)
                        .navigateUp()
                }
            }
        }
    }

    private fun onItemClicked() {
        binding.apply {
            edtCpOpen.setOnClickListener { timeHandler(edtCpOpen) }
            edtCpClose.setOnClickListener { timeHandler(edtCpClose) }
            btnSubmit.setOnClickListener {
                getServiceId?.let { it1 -> updateService(it1) }
            }
            btnAddService.setOnClickListener {
                postService()
            }
        }
    }

    private fun getListFromDays(): List<String> {
        val listDay = arrayListOf<String>()
        binding.apply {
            if (chkOpenSunday.isChecked) listDay.add(StatusHelper.DAY_SUNDAY.toString())
            if (chkOpenMonday.isChecked) listDay.add(StatusHelper.DAY_MONDAY.toString())
            if (chkOpenTuesday.isChecked) listDay.add(StatusHelper.DAY_TUESDAY.toString())
            if (chkOpenWednesday.isChecked) listDay.add(StatusHelper.DAY_WEDNESDAY.toString())
            if (chkOpenThursday.isChecked) listDay.add(StatusHelper.DAY_THURSDAY.toString())
            if (chkOpenFriday.isChecked) listDay.add(StatusHelper.DAY_FRIDAY.toString())
            if (chkOpenSaturday.isChecked) listDay.add(StatusHelper.DAY_SATURDAY.toString())
        }
        return listDay
    }

    private fun timeHandler(editText: EditText) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar[Calendar.HOUR_OF_DAY]
        val currentMinutes = calendar[Calendar.MINUTE]

        val timePicker = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val formatHour = DateHelper.timeUnderTen(hour)
            val formatMinute = DateHelper.timeUnderTen(minute)
            editText.setText("$formatHour:$formatMinute")
        }

        val timePickerDialog =
            TimePickerDialog(requireContext(), timePicker, currentHour, currentMinutes, true)
        timePickerDialog.show()
    }

    private fun isEditTextNull(): Boolean {
        var isNullOrEmpty: Boolean
        binding.apply {
            isNullOrEmpty = edtServiceName.text.isNullOrEmpty() || edtCpOpen.text.isNullOrEmpty() ||
                    edtCpClose.text.isNullOrEmpty() || edtServiceMax.text.isNullOrEmpty() ||
                    edtServiceCashier.text.isNullOrEmpty()
        }
        if (isNullOrEmpty) Toast.makeText(
            context,
            getString(R.string.field_cannot_empty),
            Toast.LENGTH_SHORT
        ).show()
        return isNullOrEmpty
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_SERVICE_ID = "extra_service_domain"
    }
}