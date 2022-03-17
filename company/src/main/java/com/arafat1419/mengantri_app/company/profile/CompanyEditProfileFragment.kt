package com.arafat1419.mengantri_app.company.profile

import android.app.Activity
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyEditProfileBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.utils.DateHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.properties.Delegates


@ExperimentalCoroutinesApi
@FlowPreview
class CompanyEditProfileFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyEditProfileBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: CompanyProfileViewModel by viewModel()

    private lateinit var sessionManager: CustomerSessionManager

    private lateinit var outputDirectory: File

    private lateinit var companyBannerId: String
    private lateinit var companyLogoId: String
    private var categoryMap = mutableMapOf<String, Int>()

    private var isBanner = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyEditProfileBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        // Initialize session manager from customer session manager
        sessionManager = CustomerSessionManager(requireContext())

        outputDirectory = getOutputDirectory()

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    val selectedImage: Uri = intent?.data!!
                    val realPath = createCopyAndReturnRealPath(requireContext(), intent.data!!)?.toUri()
                    val file = File(realPath.toString())

                    binding?.apply {
                        if (isBanner) {
                            imgCpBanner.apply {
                                val fileName = "${sessionManager.fetchCustomerId()}_banner.jpg"
                                setImageURI(selectedImage)
                                scaleType = ImageView.ScaleType.FIT_XY
                                viewModel.postUploadFile(fileName, isBanner, file).observe(viewLifecycleOwner) {
                                    companyBannerId = it.fileId!!
                                }
                            }
                        } else {
                            imgCpLogo.apply {
                                val fileName = "${sessionManager.fetchCustomerId()}_logo.jpg"
                                setImageURI(selectedImage)
                                scaleType = ImageView.ScaleType.FIT_XY
                                viewModel.postUploadFile(fileName, isBanner, file).observe(viewLifecycleOwner) {
                                    companyLogoId = it.fileId!!
                                }
                            }
                        }
                    }
                }
            }

        binding?.apply {
            imgCpBanner.setOnClickListener {
                Intent(Intent.ACTION_GET_CONTENT).also {
                    it.type = "image/*"
                    startForResult.launch(it)
                    isBanner = true
                }
            }

            imgCpLogo.setOnClickListener {
                Intent(Intent.ACTION_GET_CONTENT).also {
                    it.type = "image/*"
                    startForResult.launch(it)
                    isBanner = false
                }
            }

            edtCpOpen.setOnClickListener {
                timeHandler(edtCpOpen)
            }
            edtCpClose.setOnClickListener {
                timeHandler(edtCpClose)
            }
            btnCpSave.setOnClickListener {
                viewModel.postCompany(
                    CompanyDomain(
                        customerId = sessionManager.fetchCustomerId(),
                        companyName = edtCpName.text.toString(),
                        companyPhone = edtCpPhone.text.toString(),
                        companyBanner = companyBannerId,
                        companyImage = companyLogoId,
                        categoryId = categoryMap[spnCpCategory.text.toString()],
                        companyAddress = edtCpAddress.text.toString(),
                        companyCity = spnCpCity.text.toString(),
                        companyDistrics = spnCpDistrics.text.toString(),
                        companyOpenTime = edtCpOpen.text.toString(),
                        companyCloseTime = edtCpClose.text.toString()
                    )
                ).observe(viewLifecycleOwner) {
                    if (it != null) {

                    }
                }
            }
        }

        setCategories()
        spinnerHandler()
    }

    private fun createCopyAndReturnRealPath(
        context: Context, uri: Uri
    ): String? {
        val contentResolver: ContentResolver = context.contentResolver ?: return null

        val file = File(
            outputDirectory,
            if (isBanner) "${sessionManager.fetchCustomerId()}_banner.jpg" else "${sessionManager.fetchCustomerId()}_logo.jpg"
        )

        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null

            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also {
                    len = it
                } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()

        } catch (ignore: IOException) {
            return null
        }
        return file.absolutePath
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else requireActivity().filesDir
    }

    private fun setCategories() {
        var arrayCategories: Array<String>
        viewModel.getCategories().observe(viewLifecycleOwner) { listCategories ->
            arrayCategories = arrayOf()
            listCategories.forEach {
                categoryMap[it.categoryName!!] = it.categoryId!!
                arrayCategories += it.categoryName!!
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                arrayCategories
            )
            binding?.spnCpCategory?.setAdapter(adapter)
            adapter.setNotifyOnChange(true)
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

    private fun spinnerHandler() {
        binding?.apply {
            val listProvinces = mutableListOf<ProvinceDomain>()
            val listCities = mutableListOf<CityDomain>()

            viewModel.getProvinces().observe(viewLifecycleOwner) { listProvincesDomain ->
                var arrayProvinces = arrayOf<String>()
                listProvinces.addAll(listProvincesDomain)
                if (listProvincesDomain != null) {
                    listProvinces.forEach {
                        arrayProvinces += it.provinceName!!
                    }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        arrayProvinces
                    )
                    spnCpProvince.setAdapter(adapter)
                    adapter.setNotifyOnChange(true)
                }
            }

            spnCpProvince.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    spnCpCity.setText("")
                    spnCpDistrics.setText("")
                }

                override fun afterTextChanged(text: Editable?) {
                    var arrayCities: Array<String>
                    listProvinces.forEach { provinceDomain ->
                        if (text.toString() == provinceDomain.provinceName) {
                            viewModel.getCities(provinceDomain.id.toString())
                                .observe(viewLifecycleOwner) { listCitiesDomain ->
                                    listCities.clear()
                                    listCities.addAll(listCitiesDomain)

                                    arrayCities = arrayOf()

                                    listCities.forEach { cityDomain ->
                                        arrayCities += cityDomain.cityName!!
                                    }
                                    val adapter = ArrayAdapter(
                                        requireContext(),
                                        android.R.layout.simple_dropdown_item_1line,
                                        arrayCities
                                    )
                                    spnCpCity.setAdapter(adapter)
                                    adapter.setNotifyOnChange(true)
                                }
                        }
                    }
                }
            })

            spnCpCity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    spnCpDistrics.setText("")
                }

                override fun afterTextChanged(text: Editable?) {
                    var arrayDistrics: Array<String>
                    listCities.forEach { citiesDomain ->
                        if (text.toString() == citiesDomain.cityName) {
                            viewModel.getDistrics(citiesDomain.id.toString())
                                .observe(viewLifecycleOwner) { listDistricsDomain ->
                                    arrayDistrics = arrayOf()

                                    listDistricsDomain?.forEach {
                                        arrayDistrics += it.districsName!!
                                    }
                                    val adapter = ArrayAdapter(
                                        requireContext(),
                                        android.R.layout.simple_dropdown_item_1line,
                                        arrayDistrics
                                    )
                                    spnCpDistrics.setAdapter(adapter)
                                    adapter.setNotifyOnChange(true)
                                }
                        }
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val IS_BANNER = "is_banner"
    }
}