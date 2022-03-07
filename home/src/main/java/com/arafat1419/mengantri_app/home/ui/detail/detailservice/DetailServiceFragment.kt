package com.arafat1419.mengantri_app.home.ui.detail.detailservice

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.assets.R
import com.arafat1419.mengantri_app.core.domain.model.ServiceCountDomain
import com.arafat1419.mengantri_app.core.domain.model.TicketDomain
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.home.databinding.FragmentDetailServiceBinding
import com.arafat1419.mengantri_app.home.databinding.ModalDetailServiceBinding
import com.arafat1419.mengantri_app.home.di.homeModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalCoroutinesApi
@FlowPreview
class DetailServiceFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentDetailServiceBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: DetailServiceViewModel by viewModel()

    private var navHostFragment: Fragment? = null

    private lateinit var dialogData: MutableMap<String, Any>

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

        dialogData = mutableMapOf()

        val getServiceDomain =
            arguments?.getParcelable<ServiceCountDomain>(EXTRA_SERVICE_COUNT_DOMAIN)
        val companyName = arguments?.getString(EXTRA_COMPANY_NAME)

        if (getServiceDomain != null && companyName != null) {
            showDataService(getServiceDomain, companyName)
            dialogData[EXTRA_SERVICE_ID] = getServiceDomain.services.serviceId!!
        }

        // Load koin manually for multi modules
        loadKoinModules(homeModule)

        // Initialize nav host fragment as fragment container
        navHostFragment =
            parentFragmentManager.findFragmentById(com.arafat1419.mengantri_app.R.id.fragment_container)

        binding?.apply {
            val myCalendar = Calendar.getInstance()
            edtDServiceDate.setOnClickListener {
                val date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    myCalendar.set(year, month, day)
                    edtDServiceDate.setText(updateLabel(myCalendar))
                    val postDate = returnLabel(myCalendar)
                    dialogData[EXTRA_DATE] = postDate
                }
                val datePicker = DatePickerDialog(
                    requireContext(),
                    date,
                    myCalendar[Calendar.YEAR],
                    myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH],
                )

                datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
                datePicker.show()

            }

            btnDServiceRegister.setOnClickListener {
                if (!edtDServiceDate.text.isNullOrEmpty()) {
                    showBiodataDialog()
                    val customerId = CustomerSessionManager(requireContext()).fetchCustomerId()
                    dialogData[EXTRA_CUSTOMER_ID] = customerId
                    dialogData[EXTRA_SERVICE_TIME] = txtDServiceEst.text
                } else {
                    Toast.makeText(context, "Date cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    private fun showBiodataDialog() {
        val modalBinding: ModalDetailServiceBinding =
            ModalDetailServiceBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(requireActivity())

        val metrics = requireActivity().resources.displayMetrics
        val width = (6 * metrics.widthPixels) / 7

        modalBinding.apply {
            edtModalDServiceName.width = width
            edtModalDServiceNotes.width = width
            edtModalDServicePhone.width = width
            btnModalDService.width = width

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(modalBinding.root)
            dialog.show()

            btnModalDServiceClose.setOnClickListener {
                dialog.dismiss()
            }
            btnModalDService.setOnClickListener {
                dialogData[EXTRA_PERSON_NAME] = edtModalDServiceName.text.toString()
                dialogData[EXTRA_PERSON_PHONE] = edtModalDServicePhone.text.toString()
                dialogData[EXTRA_NOTES] = edtModalDServiceNotes.text.toString()

                Log.d("Lihat", dialogData.toString())
                viewModel.postTicket(
                    dialogData[EXTRA_CUSTOMER_ID].toString().toInt(),
                    dialogData[EXTRA_SERVICE_ID].toString().toInt(),
                    dialogData[EXTRA_PERSON_NAME].toString(),
                    dialogData[EXTRA_PERSON_PHONE].toString(),
                    dialogData[EXTRA_NOTES].toString(),
                    dialogData[EXTRA_SERVICE_TIME].toString(),
                    dialogData[EXTRA_DATE].toString(),
                ).observe(viewLifecycleOwner) {
                    Log.d("Lihat", it.toString())
                    Toast.makeText(context, "Ticket has been added", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
        }
    }

    private fun updateLabel(calendar: Calendar): String {
        val myFormat = "EEEE, dd-MM-y"
        val dateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun returnLabel(calendar: Calendar): String {
        val myFormat = "y-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun showTicketDetail(served: Int, waiting: Int, total: Int, cancel: Int) {
        binding?.apply {
            txtDServiceServed.text = served.toString()
            txtDServiceWaiting.text = waiting.toString()
            txtDServiceTotal.text = total.toString()
            txtDServiceCancel.text = cancel.toString()
        }
    }

    private fun showDataService(serviceDomain: ServiceCountDomain, companyName: String) {
        binding?.apply {
            txtServicesAppTitle.text = serviceDomain.services.serviceName
            txtDServiceTitle.text = serviceDomain.services.serviceName
            txtDServiceCompany.text = companyName
            txtDServiceTime.text = resources.getString(
                R.string.time_format,
                serviceDomain.services.serviceOpenTime?.substring(0..4),
                serviceDomain.services.serviceCloseTime?.substring(0..4)
            )
            edtDServiceAnnouncement.setText(serviceDomain.services.serviceAnnouncement)
            txtDServiceEst.text = serviceDomain.services.serviceTime
            showTicketDetail(
                serviceDomain.served!!,
                serviceDomain.waiting!!,
                serviceDomain.total!!,
                serviceDomain.cancel!!
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_SERVICE_COUNT_DOMAIN = "extra_service_domain"
        const val EXTRA_COMPANY_NAME = "extra_company_name"

        const val EXTRA_CUSTOMER_ID = "extra_customer_id"
        const val EXTRA_SERVICE_ID = "extra_service_id"
        const val EXTRA_PERSON_NAME = "extra_person_name"
        const val EXTRA_PERSON_PHONE = "extra_person_phone"
        const val EXTRA_NOTES = "extra_notes"
        const val EXTRA_SERVICE_TIME = "extra_service_time"
        const val EXTRA_DATE = "extra_date"
    }
}