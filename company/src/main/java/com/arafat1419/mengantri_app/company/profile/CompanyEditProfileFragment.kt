package com.arafat1419.mengantri_app.company.profile

import android.app.TimePickerDialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.arafat1419.mengantri_app.R
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyEditProfileBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.CategoryDomain
import com.arafat1419.mengantri_app.core.domain.model.CompanyDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import com.arafat1419.mengantri_app.core.utils.CompanySessionManager
import com.arafat1419.mengantri_app.core.utils.CustomerSessionManager
import com.arafat1419.mengantri_app.core.utils.DataMapper
import com.arafat1419.mengantri_app.core.utils.DateHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyEditProfileFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyEditProfileBinding? = null
    private val binding get() = _binding!!

    // Initialize viewModel with koin
    private val viewModel: CompanyProfileViewModel by viewModel()

    private val customerSessionManager: CustomerSessionManager by lazy {
        CustomerSessionManager(
            requireContext()
        )
    }
    private val companySessionManager: CompanySessionManager by lazy {
        CompanySessionManager(
            requireContext()
        )
    }

    private var companyBannerId: String? = null
    private var companyImageId: String? = null

    private var categoryList = arrayListOf<CategoryDomain>()

    private var isBanner = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyEditProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load koin manually for multi modules
        loadKoinModules(companyModule)

        binding.apply {
            if (companySessionManager.fetchCompanyId() == -1) {
                // Add company
                txtToolbarTitle.text = getString(R.string.company_register)
            } else {
                // Update company
                txtToolbarTitle.text = getString(R.string.edit_company)
                getCompany()

                btnSubmit.visibility = View.VISIBLE

                txtNotes.visibility = View.GONE
                btnAdd.visibility = View.GONE
            }
        }

        setCategories()
        setProvince()
        onItemClicked()
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            try {
                val realPath = createCopyAndReturnRealPath(uri)
                val file = File(realPath?.toUri().toString())

                if (isBanner) {
                    binding.imgBanner.setImageURI(uri)
                    uploadFile(file)
                } else {
                    binding.imgImage.setImageURI(uri)
                    uploadFile(file)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    private fun onItemClicked() {
        binding.apply {
            btnBanner.setOnClickListener {
                isBanner = true
                galleryLauncher.launch("image/*")
            }
            btnImage.setOnClickListener {
                isBanner = false
                galleryLauncher.launch("image/*")
            }
            edtOpenTime.setOnClickListener {
                timeHandler(edtOpenTime)
            }
            edtCloseTime.setOnClickListener {
                timeHandler(edtCloseTime)
            }
            btnAdd.setOnClickListener {
                if (checkEditText()) {
                    postCompany()
                }
            }
            btnSubmit.setOnClickListener {
                updateCompany(companySessionManager.fetchCompanyId())
            }
        }
    }

    private fun getCompany() {
        viewModel.getCompany(companySessionManager.fetchCompanyId())
            .observe(viewLifecycleOwner) { company ->
                if (company != null) {
                    setData(company)
                }
            }
    }

    private fun setData(company: CompanyDomain) {
        binding.apply {
            Glide.with(this@CompanyEditProfileFragment)
                .load(DataMapper.imageDirectus + company.companyBanner)
                .into(imgBanner)

            Glide.with(this@CompanyEditProfileFragment)
                .load(DataMapper.imageDirectus + company.companyImage)
                .into(imgImage)

            edtName.setText(company.companyName)
            edtPhone.setText(company.companyPhone)
            val categoryName = categoryList.first {
                company.categoryId == it.categoryId
            }.categoryName
            spnCategory.setText(categoryName)
            edtOpenTime.setText(company.companyOpenTime?.substring(0..4))
            edtCloseTime.setText(company.companyCloseTime?.substring(0..4))
            edtAddress.setText(company.companyAddress)
            spnProvince.setText(company.companyProvince)
            spnCity.setText(company.companyCity)
            spnDistrics.setText(company.companyDistrics)

            companyBannerId = company.companyBanner
            companyImageId = company.companyImage
        }
    }

    private fun postCompany() {
        binding.apply {
            val categoryId = categoryList.first {
                spnCategory.text.toString() == it.categoryName
            }.categoryId
            viewModel.postCompany(
                CompanyDomain(
                    customerId = customerSessionManager.fetchCustomerId(),
                    companyName = edtName.text.toString(),
                    companyPhone = edtPhone.text.toString(),
                    companyBanner = companyBannerId,
                    companyImage = companyImageId,
                    categoryId = categoryId,
                    companyAddress = edtAddress.text.toString(),
                    companyProvince = spnProvince.text.toString(),
                    companyCity = spnCity.text.toString(),
                    companyDistrics = spnDistrics.text.toString(),
                    companyOpenTime = edtOpenTime.text.toString(),
                    companyCloseTime = edtCloseTime.text.toString()
                )
            ).observe(viewLifecycleOwner) { company ->
                companySessionManager.saveCompany(company)
                Toast.makeText(
                    context,
                    getString(R.string.register_company_success),
                    Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            }
        }
    }

    private fun updateCompany(companyId: Int) {
        binding.apply {
            val categoryId = categoryList.first {
                spnCategory.text.toString() == it.categoryName
            }.categoryId
            viewModel.updateCompany(
                companyId,
                CompanyDomain(
                    customerId = customerSessionManager.fetchCustomerId(),
                    companyName = edtName.text.toString(),
                    companyPhone = edtPhone.text.toString(),
                    companyBanner = companyBannerId,
                    companyImage = companyImageId,
                    categoryId = categoryId,
                    companyAddress = edtAddress.text.toString(),
                    companyProvince = spnProvince.text.toString(),
                    companyCity = spnCity.text.toString(),
                    companyDistrics = spnDistrics.text.toString(),
                    companyOpenTime = edtOpenTime.text.toString(),
                    companyCloseTime = edtCloseTime.text.toString()
                )
            ).observe(viewLifecycleOwner) { company ->
                companySessionManager.clearCompany()
                companySessionManager.saveCompany(company)
                Toast.makeText(
                    context,
                    getString(R.string.update_company_success),
                    Toast.LENGTH_SHORT
                ).show()
                NavHostFragment.findNavController(this@CompanyEditProfileFragment)
                    .navigateUp()
            }
        }
    }

    private fun uploadFile(file: File) {
        if (isBanner) {
            val fileName = "${customerSessionManager.fetchCustomerId()}_banner.jpg"
            viewModel.postUploadFile(fileName, isBanner, file).observe(viewLifecycleOwner) {
                companyBannerId = it.fileId
            }
        } else {
            val fileName = "${customerSessionManager.fetchCustomerId()}_image.jpg"
            viewModel.postUploadFile(fileName, isBanner, file).observe(viewLifecycleOwner) {
                companyImageId = it.fileId
            }
        }
    }

    private fun setCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner) { listCategories ->
            categoryList.addAll(listCategories)
            val listName = listCategories.map {
                it.categoryName
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listName
            )
            binding.spnCategory.setAdapter(adapter)
            adapter.setNotifyOnChange(true)
        }
    }

    private fun setProvince() {
        binding.apply {
            viewModel.getProvinces().observe(viewLifecycleOwner) { listProvince ->
                if (listProvince != null) {
                    val listName = listProvince.map {
                        it.provinceName
                    }

                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        listName
                    )

                    spnProvince.apply {
                        setAdapter(adapter)
                        setOnItemClickListener { _, _, i, _ ->
                            spnCity.setText("")
                            spnDistrics.setText("")

                            val selectedProvince = listProvince.first { province ->
                                province.provinceName == adapter.getItem(i)
                            }
                            setCity(selectedProvince)
                        }
                        adapter.setNotifyOnChange(true)
                    }
                }
            }
        }
    }

    private fun setCity(province: ProvinceDomain) {
        binding.apply {
            if (province.id != null) {
                viewModel.getCities(province.id.toString())
                    .observe(viewLifecycleOwner) { listCity ->
                        if (listCity != null) {
                            val listName = listCity.map {
                                it.cityName
                            }

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                listName
                            )

                            spnCity.apply {
                                setAdapter(adapter)
                                setOnItemClickListener { _, _, i, _ ->
                                    spnDistrics.setText("")

                                    val selectedCity = listCity.first { city ->
                                        city.cityName == adapter.getItem(i)
                                    }
                                    setDistrics(selectedCity)
                                }
                                adapter.setNotifyOnChange(true)
                            }
                        }
                    }
            }
        }
    }

    private fun setDistrics(city: CityDomain) {
        binding.apply {
            if (city.id != null) {
                viewModel.getDistrics(city.id.toString())
                    .observe(viewLifecycleOwner) { listDistrics ->
                        if (listDistrics != null) {
                            val listName = listDistrics.map {
                                it.districsName
                            }

                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                listName
                            )

                            spnDistrics.apply {
                                setAdapter(adapter)
                                adapter.setNotifyOnChange(true)
                            }
                        }
                    }
            }
        }
    }

    private fun createCopyAndReturnRealPath(
        uri: Uri
    ): String? {
        val contentResolver: ContentResolver = context?.contentResolver ?: return null

        var tempDir = context?.getExternalFilesDir(null)
        tempDir = File(tempDir?.absolutePath + "/.temp/")
        tempDir.mkdir()

        val tempFile = File.createTempFile("IMG_TEMP_", ".jpg", tempDir)

        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null

            val outputStream: OutputStream = FileOutputStream(tempFile)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also {
                    len = it
                } > 0) outputStream.write(buf, 0, len)
            outputStream.apply {
                flush()
                close()
            }
            inputStream.close()

        } catch (ignore: IOException) {
            return null
        }
        return tempFile.absolutePath
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

    private fun checkEditText(): Boolean {
        var check: Boolean
        binding.apply {
            val isNullOrEmpty =
                edtName.text.isNullOrEmpty() || edtPhone.text.isNullOrEmpty() || spnCategory.text.isNullOrEmpty() || edtOpenTime.text.isNullOrEmpty() ||
                        edtCloseTime.text.isNullOrEmpty() || edtAddress.text.isNullOrEmpty() ||
                        spnProvince.text.isNullOrEmpty() || spnCity.text.isNullOrEmpty() ||
                        spnDistrics.text.isNullOrEmpty()
            check = when {
                isNullOrEmpty -> {
                    Toast.makeText(context, R.string.field_cannot_empty, Toast.LENGTH_SHORT).show()
                    false
                }
                companyBannerId.isNullOrEmpty() || companyImageId.isNullOrEmpty() -> {
                    Toast.makeText(context, com.arafat1419.mengantri_app.company.R.string.banner_and_logo_cannot_empty, Toast.LENGTH_SHORT)
                        .show()
                    false
                }
                !ckbPrivacy.isChecked -> {
                    Toast.makeText(context, com.arafat1419.mengantri_app.company.R.string.agree_to_privacy_failed, Toast.LENGTH_SHORT)
                        .show()
                    false
                }
                else -> true
            }
        }
        return check
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}