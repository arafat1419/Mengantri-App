package com.arafat1419.mengantri_app.company.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.arafat1419.mengantri_app.company.databinding.FragmentCompanyEditProfileBinding
import com.arafat1419.mengantri_app.company.di.companyModule
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.CityDomain
import com.arafat1419.mengantri_app.core.domain.model.provincedomain.ProvinceDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@ExperimentalCoroutinesApi
@FlowPreview
class CompanyEditProfileFragment : Fragment() {

    // Initilize binding with null because we need to set it null again when fragment destroy
    private var _binding: FragmentCompanyEditProfileBinding? = null
    private val binding get() = _binding

    // Initialize viewModel with koin
    private val viewModel: CompanyProfileViewModel by viewModel()

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

        spinnerHandler()
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
}