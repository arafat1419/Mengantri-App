package com.arafat1419.mengantri_app.company.services.detailservices

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.company.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyDetailServiceBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.company.services.CompanyServicesViewModel
import com.arafat1419.mengantri_app.core.domain.model.ServiceDomain
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
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: CompanyServicesViewModel by viewModel()

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
    ): View? {
        _binding = FragmentCompanyDetailServiceBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getServiceDomain = arguments?.getParcelable<ServiceDomain>(EXTRA_SERVICE_DOMAIN)

        binding?.apply {
            if (getServiceDomain != null) {
                txtServicesAppTitle.text = getServiceDomain.serviceName
                btnServiceSave.text = getString(R.string.update)
                showData(getServiceDomain)

                btnServiceSave.setOnClickListener {
                    updateService(getServiceDomain.serviceId!!)
                }
            }

            edtCpOpen.setOnClickListener { timeHandler(edtCpOpen) }
            edtCpClose.setOnClickListener { timeHandler(edtCpClose) }
        }

        // Load koin manually for multi modules
        loadKoinModules(companyModule)
    }

    private fun showData(serviceDomain: ServiceDomain) {
        binding?.apply {
            edtServiceName.setText(serviceDomain.serviceName)
            edtCpOpen.setText(serviceDomain.serviceOpenTime?.substring(0..4))
            edtCpClose.setText(serviceDomain.serviceCloseTime?.substring(0..4))
            edtServiceMax.setText(serviceDomain.serviceMaxCustomer.toString())

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

    private fun updateService(serviceId: Int) {
        binding?.apply {
            val listDay = arrayListOf<String>()
            if (chkOpenSunday.isChecked) listDay.add(StatusHelper.DAY_SUNDAY.toString())
            if (chkOpenMonday.isChecked) listDay.add(StatusHelper.DAY_MONDAY.toString())
            if (chkOpenTuesday.isChecked) listDay.add(StatusHelper.DAY_TUESDAY.toString())
            if (chkOpenWednesday.isChecked) listDay.add(StatusHelper.DAY_WEDNESDAY.toString())
            if (chkOpenThursday.isChecked) listDay.add(StatusHelper.DAY_THURSDAY.toString())
            if (chkOpenFriday.isChecked) listDay.add(StatusHelper.DAY_FRIDAY.toString())
            if (chkOpenSaturday.isChecked) listDay.add(StatusHelper.DAY_SATURDAY.toString())

            viewModel.updateService(
                serviceId = serviceId,
                serviceName = edtServiceName.text.toString(),
                serviceOpenTime = edtCpOpen.text.toString(),
                serviceCloseTime = edtCpClose.text.toString(),
                serviceAnnouncement = edtDServiceAnnouncement.text.toString(),
                serviceMaxCustomer = edtServiceMax.text.toString().toInt(),
                serviceStatus = if (swcServiceShow.isChecked) 1 else 0,
                serviceDay = listDay
            ).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    Toast.makeText(context, "Update service successful", Toast.LENGTH_SHORT).show()
                    NavHostFragment.findNavController(this@CompanyDetailServiceFragment)
                        .navigateUp()
                }
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_SERVICE_DOMAIN = "extra_service_domain"
    }
}